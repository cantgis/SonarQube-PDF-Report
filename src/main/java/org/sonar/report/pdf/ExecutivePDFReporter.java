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

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.report.pdf.entity.FileInfo;
import org.sonar.report.pdf.entity.Project;
import org.sonar.report.pdf.entity.Qprofile;
import org.sonar.report.pdf.entity.Rule;
import org.sonar.report.pdf.entity.exception.ReportException;
import org.sonar.report.pdf.util.Credentials;
import org.sonar.report.pdf.util.MetricKeys;

import com.lowagie.text.Chapter;
import com.lowagie.text.ChapterAutoNumber;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;



/**
 * Executive PDF Reporter
 *
 */
public class ExecutivePDFReporter extends PDFReporter {

    /**
     * 
     */
    private static final long serialVersionUID = -5378403769337739685L;

    private static final Logger LOG = LoggerFactory.getLogger(ExecutivePDFReporter.class);

    private URL logo;
    private String projectKey;
    private Properties configProperties;
    private Properties langProperties;

    public ExecutivePDFReporter(final Credentials credentials, final URL logo, final String projectKey,
            final Properties configProperties, final Properties langProperties) {
        super(credentials);
        this.logo = logo;
        this.projectKey = projectKey;
        this.configProperties = configProperties;
        this.langProperties = langProperties;
    }

    @Override
    protected URL getLogo() {
        return this.logo;
    }

    @Override
    protected String getProjectKey() {
        return this.projectKey;
    }

    @Override
    protected Properties getLangProperties() {
        return langProperties;
    }

    @Override
    protected Properties getReportProperties() {
        return configProperties;
    }

    /**
     * @see org.sonar.report.pdf.PDFReporter#printFrontPage(com.lowagie.text.Document,
     *      com.lowagie.text.pdf.PdfWriter)
     */
    @Override
    protected void printFrontPage(final Document frontPageDocument, final PdfWriter frontPageWriter)
            throws ReportException {
        try {
            URL largeLogo;
            if (super.getConfigProperty(PDFResources.FRONT_PAGE_LOGO).startsWith(PDFResources.HTTP_PATTERN)) {
                largeLogo = new URL(super.getConfigProperty(PDFResources.FRONT_PAGE_LOGO));
            } else {
                largeLogo = this.getClass().getClassLoader()
                        .getResource(super.getConfigProperty(PDFResources.FRONT_PAGE_LOGO));
            }
            Image logoImage = Image.getInstance(largeLogo);
            logoImage.scaleAbsolute(360, 200);
            Rectangle pageSize = frontPageDocument.getPageSize();
            logoImage.setAbsolutePosition(Style.FRONTPAGE_LOGO_POSITION_X, Style.FRONTPAGE_LOGO_POSITION_Y);
            frontPageDocument.add(logoImage);

            PdfPTable title = new PdfPTable(1);
            title.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            title.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            String projectRow = super.getProject().getName();
            String versionRow = super.getProject().getMeasures().getVersion();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String dateRow = df.format(super.getProject().getMeasures().getDate());
            String descriptionRow = super.getProject().getDescription();

            title.addCell(new Phrase(projectRow, Style.FRONTPAGE_FONT_1));
            title.addCell(new Phrase(versionRow, Style.FRONTPAGE_FONT_1));
            title.addCell(new Phrase(descriptionRow, Style.FRONTPAGE_FONT_2));
            
            for (Qprofile qprofile :  super.getProject().getQprofile()) {
				title.addCell(new Phrase(qprofile.getLanguage()+':'+qprofile.getName(), Style.FRONTPAGE_FONT_3));           	
            }
            title.addCell(new Phrase(dateRow, Style.FRONTPAGE_FONT_3));
            title.setTotalWidth(pageSize.getWidth() - frontPageDocument.leftMargin() - frontPageDocument.rightMargin());
            title.writeSelectedRows(0, -1, frontPageDocument.leftMargin(), Style.FRONTPAGE_LOGO_POSITION_Y - 150,
                    frontPageWriter.getDirectContent());

        } catch (IOException | DocumentException e) {
            LOG.error("Can not generate front page", e);
        }
    }

    /**
     * @see org.sonar.report.pdf.PDFReporter#printPdfBody(com.lowagie.text.Document)
     */
    @Override
    protected void printPdfBody(final Document document) throws ReportException {
        Project project = super.getProject();
        // Chapter 1: Report Overview (Parent project)
        ChapterAutoNumber chapter1 = new ChapterAutoNumber(new Paragraph(project.getName(), Style.CHAPTER_FONT));
        chapter1.add(new Paragraph(getTextProperty(PDFResources.MAIN_TEXT_MISC_OVERVIEW), Style.NORMAL_FONT));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateRow = df.format(super.getProject().getMeasures().getDate());
        StringBuilder qprofiles = new StringBuilder();
        int i = 0;
        for (Qprofile qprofile : project.getQprofile()) {
        	qprofiles.append("  "+qprofile.getLanguage()+":"+qprofile.getName());
        	i += qprofile.getQprofileRules().size();
        	
        }
        chapter1.add(new Paragraph(getTextProperty(PDFResources.MAIN_TEXT_MISC_OVERVIEW1)+project.getName()+getTextProperty(PDFResources.MAIN_TEXT_MISC_OVERVIEW2)
        		+dateRow+getTextProperty(PDFResources.MAIN_TEXT_MISC_OVERVIEW3)+qprofiles.toString()+getTextProperty(PDFResources.MAIN_TEXT_MISC_OVERVIEW4)+i+getTextProperty(PDFResources.MAIN_TEXT_MISC_OVERVIEW5),Style.NORMAL_FONT));
        printDetailsForProject(project, chapter1);
        try {
            document.add(chapter1);
            List<Project> projects = project.getSubprojects();
            if (projects.size()>1) {
            	 for (Project subProject : projects) {
                     ChapterAutoNumber chapterN = new ChapterAutoNumber(
                             new Paragraph(subProject.getName(), Style.CHAPTER_FONT));
                     printDetailsForProject(subProject, chapterN);
                     document.add(chapterN);
                 }
            }
           
        } catch (DocumentException e) {
            throw new ReportException("Error printing PDF Body", e);
        }

    }

    /**
     * Print details for Project
     * 
     * @param project
     *            project
     * @param chapter
     *            chapter
     * @throws ReportException
     *             ReportException
     */
    private void printDetailsForProject(Project project, Chapter chapter) throws ReportException {
        Section sectionN1 = chapter
                .addSection(new Paragraph(getTextProperty(PDFResources.GENERAL_REPORT_OVERVIEW), Style.TITLE_FONT));
        printDashboard(project, sectionN1);
        Section sectionN2 = chapter
                .addSection(new Paragraph(getTextProperty(PDFResources.GENERAL_VIOLATIONS_ANALYSIS), Style.TITLE_FONT));
        printMostViolatedRules(project, sectionN2);
        printMostViolatedFiles(project, sectionN2);
        printMostComplexFiles(project, sectionN2);
        printMostDuplicatedFiles(project, sectionN2);
        printSpecificData(project, chapter);
    }

    /**
     * Print specific data for project (use this to implement specific rendering
     * section)
     * 
     * @param project
     *            project
     * @param chapter
     *            chapter
     * @throws ReportException
     *             ReportException
     */
    protected void printSpecificData(Project project, Chapter chapter) throws ReportException {
        // nothing to do here; used for inherited class to insert specific data

    }

    /**
     * Print dashboard for project
     * 
     * @param project
     *            project
     * @param section
     *            section
     * @throws ReportException
     *             ReportException
     */
    protected void printDashboard(final Project project, final Section section) {
        section.add(Chunk.NEWLINE);
        // Coding issues analysis
        section.add(createCodingRuleViolations(project));
        // Coding issues details
        section.add(createCodingRuleViolationsDetails(project));
        // Static Analysis
        section.add(createStaticAnalysis(project));
        // Dynamic Analysis
        section.add(createDynamicAnalysis(project));
       
    }

    /**
     * Create rule violations details table
     * 
     * @param project
     *            project
     * @return The table (iText table) ready to add to the document
     */
    private PdfPTable createCodingRuleViolationsDetails(final Project project) {
    	 PdfPTable codeAnalysisTable = new PdfPTable(3);
         Style.noBorderTable(codeAnalysisTable);
         codeAnalysisTable.setWidthPercentage(80);
         
         PdfPTable violations = new PdfPTable(1);
         Style.noBorderTable(violations);
         violations.addCell(new Phrase(project.getMeasure(MetricKeys.VIOLATIONS).getFormatValue(), Style.DASHBOARD_DATA_FONT));
         violations.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_VIOLATIONS), Style.NORMAL_FONT));   
         
         PdfPTable violationsname = new PdfPTable(1);
         Style.noBorderTable(violationsname);
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_OPEN_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_REOPENED_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_CONFIRMED_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_FALSE_POSITIVE_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_WONT_FIX_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_FIXED_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_REMOVED_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_BLOCKER_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_CRITICAL_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_MAJOR_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_MINOR_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));
         violationsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_INFO_VIOLATIONS), Style.DASHBOARD_DATA_FONT_2));

         PdfPTable violationsvalue = new PdfPTable(1);
         Style.noBorderTable(violationsvalue);
         Style.alignCenterTable(violationsvalue);
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.OPEN_ISSUES).getFormatValue() , Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.REOPENED_ISSUES).getFormatValue() , Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.CONFIRMED_ISSUES).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.FALSE_POSITIVE_ISSUES).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.WONT_FIX_ISSUES).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getFixedissues().toString(), Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getRemovedissues().toString(), Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.BLOCKER_VIOLATIONS).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.CRITICAL_VIOLATIONS).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.MAJOR_VIOLATIONS).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.MINOR_VIOLATIONS).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
         violationsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.INFO_VIOLATIONS).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));        
         codeAnalysisTable.setSpacingBefore(10);
         codeAnalysisTable.addCell(violations);
         codeAnalysisTable.addCell(violationsname);
         codeAnalysisTable.addCell(violationsvalue);
         codeAnalysisTable.setSpacingAfter(20);
         return codeAnalysisTable;
    }

    /**
     * Create a table for a project, a mesure and a text
     * 
     * @param project
     *            project
     * @param text
     *            text
     * @param measure
     *            measure
     * @return The table (iText table) ready to add to the document
     */

    private PdfPTable createTable(final Project project, String text, MetricKeys measure) {
        PdfPTable criticalViolations = new PdfPTable(1);
        Style.noBorderTable(criticalViolations);
        criticalViolations.addCell(new Phrase(getTextProperty(text), Style.DASHBOARD_TITLE_FONT));
        PdfPTable criticalViolationsTendency = new PdfPTable(2);
        Style.noBorderTable(criticalViolationsTendency);
        criticalViolationsTendency.getDefaultCell().setFixedHeight(Style.TENDENCY_ICONS_HEIGHT);
        criticalViolationsTendency
                .addCell(new Phrase(project.getMeasure(measure).getFormatValue(), Style.DASHBOARD_DATA_FONT));
		criticalViolationsTendency.addCell("");

        criticalViolations.addCell(criticalViolationsTendency);
        return criticalViolations;
    }

    /**
     * Create coding rule violations for a project
     * 
     * @param project
     *            project
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    private Paragraph createCodingRuleViolations(final Project project) {
        Paragraph codingRulesViolations = new Paragraph(getTextProperty(PDFResources.GENERAL_CODING_RULES_VIOLATIONS),
                Style.UNDERLINED_FONT);
		//bugs 
        PdfPTable bugsViolationsTable = new PdfPTable(2);
        Style.noBorderTable(bugsViolationsTable);
        bugsViolationsTable.setSpacingBefore(10);
		bugsViolationsTable
                .addCell(createTable(project, PDFResources.GENERAL_BUGS, MetricKeys.BUGS));       
		bugsViolationsTable
                .addCell(createTable(project, PDFResources.GENERAL_RELIABILITY_REMEDIATION_EFFORT, MetricKeys.RELIABILITY_REMEDIATION_EFFORT));
        bugsViolationsTable.setSpacingAfter(10);
        codingRulesViolations.add(bugsViolationsTable);
		//security
		PdfPTable secutityViolationsTable = new PdfPTable(2);
        Style.noBorderTable(secutityViolationsTable);
        secutityViolationsTable.setSpacingBefore(10);		
		secutityViolationsTable
                .addCell(createTable(project, PDFResources.GENERAL_SECURITY, MetricKeys.SECURITY));
		secutityViolationsTable
                .addCell(createTable(project, PDFResources.GENERAL_SECUTITY_REMEDIATION_EFFORT, MetricKeys.SECUTITY_REMEDIATION_EFFORT));
        secutityViolationsTable.setSpacingAfter(10);
        codingRulesViolations.add(secutityViolationsTable);
		//bad smell 
		PdfPTable maintainabilityViolationsTable = new PdfPTable(2);
        Style.noBorderTable(maintainabilityViolationsTable);
        maintainabilityViolationsTable.setSpacingBefore(10);				
		maintainabilityViolationsTable
                .addCell(createTable(project, PDFResources.GENERAL_MAINTAINABILITY, MetricKeys.MAINTAINABILITY));
		maintainabilityViolationsTable
                .addCell(createTable(project, PDFResources.GENERAL_TECHNICAL_DEBT, MetricKeys.TECHNICAL_DEBT));
        maintainabilityViolationsTable.setSpacingAfter(10);
        codingRulesViolations.add(maintainabilityViolationsTable);
        return codingRulesViolations;
    }

    /**
     * Create dynamic analysis content for a project
     * 
     * @param project
     *            project
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    private Paragraph createDynamicAnalysis(final Project project) {
    	if (project.getMeasure(MetricKeys.TESTS).getFormatValue() == "N/A" || project.getMeasure(MetricKeys.TESTS).getFormatValue() == "0") {
    		return new Paragraph("");
    	}
        Paragraph dynamicAnalysis = new Paragraph(getTextProperty(PDFResources.GENERAL_DYNAMIC_ANALYSIS),
                Style.UNDERLINED_FONT);
        PdfPTable dynamicAnalysisTable = new PdfPTable(4);
        Style.noBorderTable(dynamicAnalysisTable);
        dynamicAnalysisTable.setWidthPercentage(90);

        PdfPTable codeCoverage = new PdfPTable(1);
        Style.noBorderTable(codeCoverage);
        codeCoverage.addCell(new Phrase(project.getMeasure(MetricKeys.COVERAGE).getFormatValue(), Style.DASHBOARD_DATA_FONT));
        codeCoverage
                .addCell(new Phrase(getTextProperty(PDFResources.GENERAL_CODE_COVERAGE), Style.DASHBOARD_TITLE_FONT));
       
        PdfPTable unitCoverage = new PdfPTable(1);
        Style.noBorderTable(unitCoverage);
        unitCoverage.addCell(new Phrase(project.getMeasure(MetricKeys.TESTS).getFormatValue(), Style.DASHBOARD_DATA_FONT));
        unitCoverage.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_TESTS), Style.DASHBOARD_TITLE_FONT));

        PdfPTable testname = new PdfPTable(1);
        Style.noBorderTable(testname);  
        testname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_LINE_COVERAGE), Style.DASHBOARD_DATA_FONT_2));
        testname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_BRANCHCOVERAGE), Style.DASHBOARD_DATA_FONT_2));
        testname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_FAILURES), Style.DASHBOARD_DATA_FONT_2));
        testname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_ERRORS), Style.DASHBOARD_DATA_FONT_2));
        testname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_SKIPS), Style.DASHBOARD_DATA_FONT_2));
        testname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_TEST_SUCCESS), Style.DASHBOARD_DATA_FONT_2));       

        
        PdfPTable testvalue = new PdfPTable(1);
        Style.noBorderTable(testvalue);
        Style.alignRightTable(testvalue);
        testvalue.addCell(new Phrase(project.getMeasure(MetricKeys.LINE_COVERAGE).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
        testvalue.addCell(new Phrase(project.getMeasure(MetricKeys.BRANCH_COVERAGE).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
        testvalue.addCell(new Phrase(project.getMeasure(MetricKeys.TEST_FAILURES).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
        testvalue.addCell(new Phrase(project.getMeasure(MetricKeys.TEST_ERRORS).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
        testvalue.addCell(new Phrase(project.getMeasure(MetricKeys.SKIPPED_TESTS).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
        testvalue.addCell(new Phrase(project.getMeasure(MetricKeys.TEST_SUCCESS_DENSITY).getFormatValue(),Style.DASHBOARD_DATA_FONT_2));


        dynamicAnalysisTable.setSpacingBefore(10);
        dynamicAnalysisTable.addCell(codeCoverage);
        dynamicAnalysisTable.addCell(unitCoverage);
        dynamicAnalysisTable.addCell(testname);
        dynamicAnalysisTable.addCell(testvalue);
        dynamicAnalysisTable.addCell("");
        dynamicAnalysisTable.setSpacingAfter(20);
        dynamicAnalysis.add(dynamicAnalysisTable);
        return dynamicAnalysis;
    }

    /**
     * Create static analysis content for a project
     * 
     * @param project
     *            project
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    private Paragraph createStaticAnalysis(final Project project) {
    	Paragraph staticAnalysis = new Paragraph();
    	staticAnalysis.add(new Chunk(getTextProperty(PDFResources.GENERAL_STATIC_ANALYSIS),
                Style.UNDERLINED_FONT));
    	staticAnalysis.add(new Chunk("\n",Style.NORMAL_FONT));        
        staticAnalysis.add(new Chunk(getTextProperty(PDFResources.GENERAL_SIZE),Style.NORMAL_FONT));
        staticAnalysis.add(createSizeAnalysis(project));
        staticAnalysis.add(new Chunk("\n",Style.NORMAL_FONT));
        staticAnalysis.add(new Chunk(getTextProperty(PDFResources.GENERAL_COMPLEXITY),Style.NORMAL_FONT));
        staticAnalysis.add(createComplexityAnalysis(project));
        staticAnalysis.add(new Chunk(getTextProperty(PDFResources.GENERAL_COMMENTS),Style.NORMAL_FONT));
        staticAnalysis.add(createDocumentationAnalysis(project));
        return staticAnalysis;
    }


    /**
     * Print most duplicated files for a project, and the target section
     * 
     * @param project
     *            project
     * @param section
     *            section
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    protected void printMostDuplicatedFiles(final Project project, final Section section) {
        List<FileInfo> files = project.getMostDuplicatedFiles();
        List<String> left = new LinkedList<>();
        List<String> right = new LinkedList<>();
        for (FileInfo file : files) {
            left.add(file.getName());
            right.add(file.getDuplicatedLines());
        }

        PdfPTable mostDuplicatedFilesTable = Style.createSimpleTable(left, right,
                getTextProperty(PDFResources.GENERAL_MOST_DUPLICATED_FILES),
                getTextProperty(PDFResources.GENERAL_NO_DUPLICATED_FILES));
        section.add(mostDuplicatedFilesTable);
    }

    /**
     * Print most complex files for a project, and the target section
     * 
     * @param project
     *            project
     * @param section
     *            section
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    protected void printMostComplexFiles(final Project project, final Section section) {
        List<FileInfo> files = project.getMostComplexFiles();
        List<String> left = new LinkedList<>();
        List<String> right = new LinkedList<>();
        for (FileInfo file : files) {
            left.add(file.getName());
            right.add(file.getComplexity());
        }
        PdfPTable mostComplexFilesTable = Style.createSimpleTable(left, right,
                getTextProperty(PDFResources.GENERAL_MOST_COMPLEX_FILES),
                getTextProperty(PDFResources.GENERAL_NO_COMPLEX_FILES));
        section.add(mostComplexFilesTable);
    }

    /**
     * Print most violated rules for a project, and the target section
     * 
     * @param project
     *            project
     * @param section
     *            section
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    protected void printMostViolatedRules(final Project project, final Section section) {
        List<Rule> mostViolatedRules = project.getMostViolatedRuleslist();
        Iterator<Rule> it = mostViolatedRules.iterator();
        List<String> left = new LinkedList<>();
        List<String> right = new LinkedList<>();
        List<Color> colors = new LinkedList<>();
        int limit = 0;
        while (it.hasNext() && limit < 10) {
            Rule rule = it.next();
            left.add(rule.getName());
            right.add(rule.getViolationsNumber());
            colors.add(rule.getSeverity().getColor());
            limit++;
        }

        PdfPTable mostViolatedRulesTable = Style.createSimpleTable(left, right, colors,
                getTextProperty(PDFResources.GENERAL_MOST_VIOLATED_RULES),
                getTextProperty(PDFResources.GENERAL_NO_VIOLATED_RULES));
        section.add(mostViolatedRulesTable);
    }

    /**
     * Print most violated files for a project, and the target section
     * 
     * @param project
     *            project
     * @param section
     *            section
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    protected void printMostViolatedFiles(final Project project, final Section section) {
        List<FileInfo> files = project.getMostViolatedFiles();
        List<String> left = new LinkedList<>();
        List<String> right = new LinkedList<>();
        for (FileInfo file : files) {
            left.add(file.getName());
            right.add(file.getViolations());
        }

        PdfPTable mostViolatedFilesTable = Style.createSimpleTable(left, right,
                getTextProperty(PDFResources.GENERAL_MOST_VIOLATED_FILES),
                getTextProperty(PDFResources.GENERAL_NO_VIOLATED_FILES));
        section.add(mostViolatedFilesTable);
    }
    
    /**
     * Print size for a project
     * 
     * @param project
     *            project
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    private PdfPTable createSizeAnalysis(final Project project) {
        PdfPTable staticAnalysisTable = new PdfPTable(3);
        Style.noBorderTable(staticAnalysisTable);
        staticAnalysisTable.setWidthPercentage(90);
        
        PdfPTable linesOfCode = new PdfPTable(1);
        Style.noBorderTable(linesOfCode);
        Style.alignCenterTable(linesOfCode);
        linesOfCode.addCell(new Phrase(project.getMeasure(MetricKeys.NCLOC).getFormatValue(), Style.DASHBOARD_DATA_FONT));
        linesOfCode.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_LINES_OF_CODE), Style.NORMAL_FONT));   
        
        PdfPTable linesOfCodename = new PdfPTable(1);
        Style.noBorderTable(linesOfCodename);
        linesOfCodename.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_LINES), Style.DASHBOARD_DATA_FONT_2));
        linesOfCodename.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_METHODS), Style.DASHBOARD_DATA_FONT_2));
        linesOfCodename.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_CLASSES), Style.DASHBOARD_DATA_FONT_2));
        linesOfCodename.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_FILES), Style.DASHBOARD_DATA_FONT_2));
        linesOfCodename.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_PACKAGES), Style.DASHBOARD_DATA_FONT_2));
        linesOfCodename.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_DUPLICATED_LINES), Style.DASHBOARD_DATA_FONT_2));

        PdfPTable linesOfCodevalue = new PdfPTable(1);
        Style.noBorderTable(linesOfCodevalue);
        Style.alignCenterTable(linesOfCodevalue);
        linesOfCodevalue.addCell(new Phrase(project.getMeasure(MetricKeys.LINES).getFormatValue() , Style.DASHBOARD_DATA_FONT_2));
        linesOfCodevalue.addCell(new Phrase(project.getMeasure(MetricKeys.FUNCTIONS).getFormatValue() , Style.DASHBOARD_DATA_FONT_2));
        linesOfCodevalue.addCell(new Phrase(project.getMeasure(MetricKeys.CLASSES).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
        linesOfCodevalue.addCell(new Phrase(project.getMeasure(MetricKeys.FILES).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
        linesOfCodevalue.addCell(new Phrase(project.getMeasure(MetricKeys.DIRECTORIES).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
        linesOfCodevalue.addCell(new Phrase(project.getMeasure(MetricKeys.DUPLICATED_LINES_DENSITY).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));

        staticAnalysisTable.setSpacingBefore(10);
        staticAnalysisTable.addCell(linesOfCode);
        staticAnalysisTable.addCell(linesOfCodename);
        staticAnalysisTable.addCell(linesOfCodevalue);
        staticAnalysisTable.setSpacingAfter(20);
        return staticAnalysisTable;
    }
    
    
    /**
     * Print size for a project
     * 
     * @param project
     *            project
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    private PdfPTable createComplexityAnalysis(final Project project) {
        PdfPTable staticAnalysisTable = new PdfPTable(3);
        Style.noBorderTable(staticAnalysisTable);
        staticAnalysisTable.setWidthPercentage(90);
               
        PdfPTable complexity = new PdfPTable(1);
        Style.noBorderTable(complexity);
        Style.alignCenterTable(complexity);
        complexity.addCell(new Phrase(project.getMeasure(MetricKeys.COMPLEXITY).getFormatValue(),
                Style.DASHBOARD_DATA_FONT));
        complexity.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_COMPLEXITY), Style.NORMAL_FONT));
        
        PdfPTable complexityname = new PdfPTable(1);
        Style.noBorderTable(complexityname);
       
       // complexityname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_PER_CLASS), Style.DASHBOARD_DATA_FONT_2));
        //complexityname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_DECISION_POINTS), Style.DASHBOARD_DATA_FONT_2));
        complexityname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_FILES), Style.DASHBOARD_DATA_FONT_2));
        
        PdfPTable complexityvalue = new PdfPTable(1);
        Style.noBorderTable(complexityvalue);
        Style.alignCenterTable(complexityvalue);
       // complexityvalue.addCell(new Phrase(project.getMeasure(MetricKeys.CLASS_COMPLEXITY).getFormatValue(),Style.DASHBOARD_DATA_FONT_2));
        //complexityvalue.addCell(new Phrase(project.getMeasure(MetricKeys.FUNCTION_COMPLEXITY).getFormatValue(),Style.DASHBOARD_DATA_FONT_2));
        complexityvalue.addCell(new Phrase(project.getMeasure(MetricKeys.FILE_COMPLEXITY).getFormatValue(),Style.DASHBOARD_DATA_FONT_2));
        
        staticAnalysisTable.setSpacingBefore(10);
        staticAnalysisTable.addCell(complexity);
        staticAnalysisTable.addCell(complexityname);
        staticAnalysisTable.addCell(complexityvalue);
        staticAnalysisTable.setSpacingAfter(20);
        return staticAnalysisTable;
    }

    
    /**
     * Print size for a project
     * 
     * @param project
     *            project
     * @return The paragraph (iText paragraph) ready to add to the document
     */
    private PdfPTable createDocumentationAnalysis(final Project project) {  
        PdfPTable staticAnalysisTable = new PdfPTable(3);
        Style.noBorderTable(staticAnalysisTable);
        staticAnalysisTable.setWidthPercentage(90);
        PdfPTable comments = new PdfPTable(1);
        Style.noBorderTable(comments);
        Style.alignCenterTable(comments); 
        comments.addCell(new Phrase(project.getMeasure(MetricKeys.COMMENT_LINES_DENSITY).getFormatValue(),
                Style.DASHBOARD_DATA_FONT));
        comments.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_COMMENTS), Style.DASHBOARD_TITLE_FONT));
        
        
        PdfPTable commentsname = new PdfPTable(1);
        Style.noBorderTable(commentsname);
        commentsname.addCell(new Phrase(getTextProperty(PDFResources.GENERAL_COMMENT_LINES), Style.DASHBOARD_DATA_FONT_2));
        
        PdfPTable commentsvalue = new PdfPTable(1);
        Style.noBorderTable(commentsvalue);
        Style.alignCenterTable(commentsvalue);
        commentsvalue.addCell(new Phrase(project.getMeasure(MetricKeys.COMMENT_LINES).getFormatValue(), Style.DASHBOARD_DATA_FONT_2));
        
        staticAnalysisTable.setSpacingBefore(10);
        staticAnalysisTable.addCell(comments);
        staticAnalysisTable.addCell(commentsname);
        staticAnalysisTable.addCell(commentsvalue);
        staticAnalysisTable.setSpacingAfter(20);
        return staticAnalysisTable;
    }


    /**
     * @see org.sonar.report.pdf.PDFReporter#printTocTitle(org.sonar.report.pdf.Toc)
     */
    @Override
    protected void printTocTitle(final Toc tocDocument) throws ReportException {
        Paragraph tocTitle = new Paragraph(super.getTextProperty(PDFResources.MAIN_TABLE_OF_CONTENTS),
                Style.TOC_TITLE_FONT);
        tocTitle.setAlignment(Element.ALIGN_CENTER);
        try {
            tocDocument.getTocDocument().add(tocTitle);
            tocDocument.getTocDocument().add(Chunk.NEWLINE);
        } catch (DocumentException e) {
            throw new ReportException("Error printing TOC", e);
        }
    }

    /**
     * @see org.sonar.report.pdf.PDFReporter#getReportType()
     */
    @Override
    public String getReportType() {
        return PDFResources.EXECUTIVE_REPORT_TYPE;
    }
}
