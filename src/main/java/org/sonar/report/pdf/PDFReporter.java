/*
 * SonarQube PDF Report
 * Copyright (C) 2010-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.report.pdf;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import com.lowagie.text.pdf.SimpleBookmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.report.pdf.builder.ProjectBuilder;
import org.sonar.report.pdf.entity.Project;
import org.sonar.report.pdf.entity.exception.ReportException;
import org.sonar.report.pdf.util.Credentials;
import org.sonarqube.ws.client.WSClient;
import org.sonarqube.ws.model.Tasks;
import org.sonarqube.ws.query.CeTaskQuery;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This is the superclass of concrete reporters. It provides the access to Sonar
 * data (project, measures, graphics) and report config data.
 * 
 * The concrete reporter class will provide: sonar base URL, logo (it will be
 * used in yhe PDF document), the project key and the implementation of
 * printPdfBody method.
 */
public abstract class PDFReporter implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -923944005149556486L;

    private static final Logger LOG = LoggerFactory.getLogger(PDFReporter.class);

    private Credentials credentials;

    private Project project = null;

    public PDFReporter(final Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Get Report
     * 
     * @return ByteArrayOutputStream
     * @throws ReportException
     *             ReportException
     */
    public ByteArrayOutputStream getReport() throws ReportException {
        // Creation of documents
        Document mainDocument = new Document(PageSize.A4, 50, 50, 110, 50);
        Toc tocDocument = new Toc();
        Document frontPageDocument = new Document(PageSize.A4, 50, 50, 110, 50);
        ByteArrayOutputStream mainDocumentBaos = new ByteArrayOutputStream();
        ByteArrayOutputStream frontPageDocumentBaos = new ByteArrayOutputStream();
        PdfWriter mainDocumentWriter = null;
        PdfWriter frontPageDocumentWriter = null;
        try {
            mainDocumentWriter = PdfWriter.getInstance(mainDocument, mainDocumentBaos);
            frontPageDocumentWriter = PdfWriter.getInstance(frontPageDocument, frontPageDocumentBaos);
        } catch (DocumentException e) {
            throw new ReportException("Error instantiating PDFWriters", e);
        }

        // Events for TOC, header and pages numbers
        Events events = new Events(tocDocument, new Header(this.getLogo(), this.getProject()));
        mainDocumentWriter.setPageEvent(events);

        mainDocument.open();
        tocDocument.getTocDocument().open();
        frontPageDocument.open();

        LOG.info("Generating PDF report...");
        printFrontPage(frontPageDocument, frontPageDocumentWriter);
        printTocTitle(tocDocument);
        printPdfBody(mainDocument);
        mainDocument.close();
        tocDocument.getTocDocument().close();
        frontPageDocument.close();

        // Return the final document (with TOC)
        return createFinalReport(tocDocument, mainDocumentBaos, frontPageDocumentBaos);
    }

    /**
     * Create final report
     * 
     * @param tocDocument
     *            TOC
     * @param mainDocumentBaos
     *            main document
     * @param frontPageDocumentBaos
     *            front page
     * @return ByteArrayOutputStream
     * @throws ReportException
     *             ReportException
     */
    
    @SuppressWarnings("unchecked")
	private ByteArrayOutputStream createFinalReport(Toc tocDocument, ByteArrayOutputStream mainDocumentBaos,
            ByteArrayOutputStream frontPageDocumentBaos) throws ReportException {
        ByteArrayOutputStream finalBaos = new ByteArrayOutputStream();
        try {
            // Get Readers
            PdfReader mainDocumentReader = new PdfReader(mainDocumentBaos.toByteArray());
            PdfReader tocDocumentReader = new PdfReader(tocDocument.getTocOutputStream().toByteArray());
            PdfReader frontPageDocumentReader = new PdfReader(frontPageDocumentBaos.toByteArray());
            // New document
            List<HashMap<String, Object>> bookmarks = new ArrayList<>();
            HashMap<String, Object> map = new HashMap<>();
            map.put("Title",getTextProperty(PDFResources.MAIN_TABLE_OF_CONTENTS));
            map.put("Action", "GoTo");
            map.put("Page", 2 + " FitH 732");
            bookmarks.add(map);            
            PdfStamper stamp = new PdfStamper(mainDocumentReader,finalBaos); 
            Rectangle pagesize = tocDocumentReader.getPageSize(1);
            for (int i = 1; i <= tocDocumentReader.getNumberOfPages(); i++) {
            	stamp.insertPage(i, pagesize);
            	stamp.replacePage(tocDocumentReader,i, i);
            }
            pagesize = frontPageDocumentReader.getPageSize(1);
            stamp.insertPage(1, pagesize);
        	stamp.replacePage(frontPageDocumentReader, 1, 1);
            bookmarks.addAll(SimpleBookmark.getBookmark(mainDocumentReader));
            stamp.setOutlines(bookmarks);            
            stamp.close();          
        } catch (IOException | DocumentException e) {
            throw new ReportException("Error creating final report", e);
        }
        return finalBaos;
    }
   
    /**
     * Gets current project
     * 
     * @return Project
     * @throws ReportException
     *             ReportException
     */
    public Project getProject() throws ReportException {
        if (project == null) {
            WSClient sonar = WSClient.create(credentials.getUrl(), credentials.getUsername(),
                    credentials.getPassword()); 
            LOG.info("waiting for Compute Engine task.........");
           boolean result = false;
           while(!result) {
            	 result=waitCeTask(sonar);            	
            }
            ProjectBuilder projectBuilder = ProjectBuilder.getInstance(sonar);
            project = projectBuilder.initializeProject(getProjectKey());
        }
        return project;
    }

	private boolean waitCeTask(WSClient sonar) throws ReportException {
		Tasks tasks = null;
		boolean result = false;
		int count = 0;
		try {
				Thread.sleep(5000);
				CeTaskQuery ce = CeTaskQuery.create(getProjectKey());
				ce.setStatus("SUCCESS,FAILED,CANCELED,IN_PROGRESS,PENDING");
				tasks = sonar.find(ce);
			}
		catch (InterruptedException e) {
					LOG.info("waiting for Compute Engine task.........",e);
					Thread.currentThread().interrupt();
					}      
		catch(Exception e){
				LOG.info("Can't get Compute Engine task status.Retry.........",e); 
				count ++ ;
				if (count == 10) {
					throw new ReportException("Can't get Compute Engine task status.");
				}

			}
		if (tasks !=null && !tasks.getTasks().isEmpty()) {               	             	
		    String status = tasks.getTasks().get(0).getstatus();
		    switch(status.toLowerCase()) {
		    	case "success":
		    		result = true;
		    		break;
		    	case "failed":
		    		throw new ReportException("Compute Engine task status is failed.");
				case "canceled":
		    		throw new ReportException("Compute Engine task status is canceled.");		    		
		    	default :
		    		return false;
		    }		    	
		    } else {
		    	throw new ReportException("Can't get Compute Engine task status.");
		    }  
		  return result;  
	}

 
    public String getTextProperty(final String key) {
        return getLangProperties().getProperty(key);
    }

    public String getConfigProperty(final String key) {
        return getReportProperties().getProperty(key);
    }

    /**
     * Gets image from tendency
     * 
     * @param tendencyQualitative
     *            qualitative tendency
     * @param tendencyQuantitative
     *            quantitative tendency
     * @return Image
     */
    protected Image getTendencyImage(final int trend, boolean okWhenGrows) {
        // tendency parameters are t_qual and t_quant tags returned by
        // webservices api
        String iconName;
        if (okWhenGrows) {
            iconName = defineIconForIncreasingAwaitedTendency(trend);
        } else {
            iconName = defineIconForDecreasingAwaitedTendency(trend);
        }
        Image tendencyImage = null;
        try {
            tendencyImage = Image.getInstance(this.getClass().getResource(PDFResources.TENDENCY_DIR + iconName));
        } catch (BadElementException | IOException e) {
            LOG.error("Can not generate tendency image", e);
        }
        return tendencyImage;
    }

    private String defineIconForIncreasingAwaitedTendency(final int trend) {
        String iconName;
        switch (trend) {
        case -1:
            iconName = "-1-red.png";
            break;
        case 1:
            iconName = "1-green.png";
            break;
        default:
            iconName = "none.png";
        }
        return iconName;
    }

    private String defineIconForDecreasingAwaitedTendency(final int trend) {
        String iconName;
        switch (trend) {
        case -1:
            iconName = "-1-green.png";
            break;
        case 1:
            iconName = "1-red.png";
            break;
        default:
            iconName = "none.png";
        }
        return iconName;
    }

    /**
     * Print PDF body
     * 
     * @param document
     *            document
     * @throws ReportException
     *             ReportException
     */
    protected abstract void printPdfBody(Document document) throws ReportException;

    /**
     * Pring TOC
     * 
     * @param document
     *            document
     * @throws ReportException
     *             ReportException
     */
    protected abstract void printTocTitle(Toc document) throws ReportException;

    /**
     * Get Logo
     * 
     * @return URL
     * @throws ReportException
     *             ReportException
     */
    protected abstract URL getLogo();

    /**
     * Get project key
     * 
     * @return String
     */
    protected abstract String getProjectKey();

    /**
     * Pring front page
     * 
     * @param frontPageDocument
     *            front page document
     * @param frontPageWriter
     *            front page writer
     * @throws ReportException
     *             ReportException
     */
    protected abstract void printFrontPage(Document frontPageDocument, PdfWriter frontPageWriter)
            throws ReportException;

    /**
     * Get report properties
     * 
     * @return Properties
     */
    protected abstract Properties getReportProperties();

    /**
     * Get lang properties
     * 
     * @return Properties
     */
    protected abstract Properties getLangProperties();

    public abstract String getReportType();

}
