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
package org.sonar.report.pdf.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Request.Part;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PDFWebservice implements WebService {
	private static final Logger LOG = LoggerFactory.getLogger(PDFWebservice.class);
	private static final String PDFPATH ="pdf-files";
	private static final String ERROR ="error";
	private static final String PDF_EXTENSION = ".pdf";

	private final class Handle implements RequestHandler {
		@Override
         public void handle(Request request, Response response)  {
        	 File uploadFile = new File(PDFPATH);
             if (!uploadFile.exists()) {
                 uploadFile.mkdirs();
             }
             File file = null;
             Part part = request.paramAsPart("upload");
             if (part !=null) {
            	 String filename =part.getFileName();         		  
            	 file = new File(PDFPATH+ "/" + filename);            
             try (
            	 InputStream in = part.getInputStream();
            	 FileOutputStream os = new FileOutputStream(file)
            	){
            	 byte[] bb = new byte[1024*1024];
            	 int ch;
            	 while ((ch = in.read(bb)) > -1) {
            		 os.write(bb, 0, ch);
            	 }}catch (Exception e) {            		
            		 response.newJsonWriter()
                	 .beginObject()
                	 .prop(ERROR,"Problem uploading file. ")
                	 .endObject()
                	 .close();
            		 LOG.error("STROE PDF FILES ERROR:", e);

            	}
             }else {
            	 response.newJsonWriter()
            	 .beginObject()
            	 .prop(ERROR,"Problem getting filestream!")
            	 .endObject()
            	 .close();
             }
		}
	}
	private final class HandleGet implements RequestHandler {
		@Override
         public void handle(Request request, Response response)   {
			 String filename = request.mandatoryParam("componentKey").replace(':', '-').replace('/', '-');
			 String path = PDFPATH + "/" + filename + PDF_EXTENSION;
        	 File downloadFile = new File(path);
             if (!downloadFile.exists()) {
            	 response.newJsonWriter()
            	 .beginObject()
            	 .prop(ERROR,"Report is not available. At least one analysis is required after installing the plugin!")
            	 .endObject()
            	 .close();
             }else {
            	 response.setHeader("Content-Disposition", "attachment;filename="+filename+PDF_EXTENSION);
            	 response.stream().setMediaType("application/pdf");
				try (
				   BufferedInputStream is = new BufferedInputStream(new FileInputStream(downloadFile));
				   BufferedOutputStream os = new BufferedOutputStream(response.stream().output())
						){
	            	byte[] buffer = new byte[1024*1024];
	            	int count = 0;
	                while ((count = is.read(buffer)) > -1) {
	                	os.write(buffer,0,count); 
	                }
	            	 is.close();
	                 os.flush(); 
	            	 os.close();   
				} catch (Exception e) {
					 response.newJsonWriter()
                	 .beginObject()
                	 .prop(ERROR,"Problem downloading file. ")
                	 .endObject()
                	 .close();
            		 LOG.error("Problem downloading file:", e);
				}        	
            	 
             }           
		}
	}

@Override
   public void define(Context context) {
     NewController controller = context.createController("api/pdfreport");
     controller.setDescription("PDFReport file store/get");
     // create the URL /api/hello/show
     controller.createAction("store")
       .setDescription("PDFReport file store")
       .setHandler(new Handle());
     controller.createAction("get")
     .setDescription("PDFReport file get")
     .setHandler(new HandleGet())
     .createParam("componentKey").setDescription("component key").setRequired(true);
    controller.done();
   }
}
	

