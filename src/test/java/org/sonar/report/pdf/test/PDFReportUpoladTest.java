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
package org.sonar.report.pdf.test;


import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.sonar.report.pdf.PDFResources;
import org.sonar.report.pdf.entity.exception.ReportException;
import org.sonar.report.pdf.util.FileUploader;
import org.testng.annotations.Test;

public class PDFReportUpoladTest extends SonarPDFTest {

    /**
     * Build a PDF report for the Sonar project on "sonar.base.url" instance of
     * Sonar. The property "sonar.base.url" is set in report.properties, this
     * file will be provided by the artifact consumer.
     * 
     * The key of the project is not place in properties, this is provided in
     * execution time.
     * 
     * @throws ReportException
     */
    @Test(enabled = true, groups = { "uploadreport" }, dependsOnGroups = { "report" })
    public void uploadReportTest() throws ReportException {
        String sonarUrl = getPropertyForTest("sonar.base.url");
        String login = getPropertyForTest("sonar.base.login");
        String pwd = getPropertyForTest("sonar.base.password");

        File pdf = new File("target/testReport.pdf");
        if (pdf.exists()) {
        	try {
            FileUploader.upload(pdf, sonarUrl + PDFResources.PDF_REPORT_STORE_PATH, login, pwd);
            }catch(Exception e)
        	{
            	 assert(false);
        	}
        	
        } else {
            assert(false);
        }
    }
   
    @Test(enabled = true, groups = { "getreport" }, dependsOnGroups = { "uploadreport" })
	public void uploadReportfilegetTest() {   	
    	 String sonarUrl = getPropertyForTest("sonar.base.url");
         String username = getPropertyForTest("sonar.base.login");
         String password = getPropertyForTest("sonar.base.password");
         GetMethod fileGet = new GetMethod(sonarUrl+"/api/pdfreport/get?componentKey=testReport");
    	 HttpClient client = new HttpClient();
         if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
             client.getParams().setAuthenticationPreemptive(true);
             Credentials credentials = new UsernamePasswordCredentials(username, password);
             client.getState().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), credentials);
         }
         client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
         int status;
		try {
			 status=client.executeMethod(fileGet);
			 String str = fileGet.getResponseBodyAsString();
			 System.out.println(str);
			 if (status == HttpStatus.SC_OK && !str.contains("error")) {
	        	 assert(true);
	         } else {
	        	 assert(false);
	         }
		} 
        catch (HttpException e) {
        	assert(false);
 		} catch (IOException e) {
 			assert(false);
 		}

    }
}