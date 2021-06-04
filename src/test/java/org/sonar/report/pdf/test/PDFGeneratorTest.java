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

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.sonar.report.pdf.PDFReporter;
import org.sonar.report.pdf.PDFResources;
import org.sonar.report.pdf.TeamWorkbookPDFReporter;
import org.sonar.report.pdf.entity.exception.ReportException;
import org.sonar.report.pdf.util.Credentials;
import org.testng.annotations.Test;

public class PDFGeneratorTest extends SonarPDFTest {

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
    @Test(enabled = true, groups = { "report" }, dependsOnGroups = { "metrics" })
    public void getReportTest() throws ReportException {
        String logo = getPropertyForTest("front.page.logo");
        String sonarUrl = getPropertyForTest("sonar.base.url");
        String projectKey = getPropertyForTest("sonar.projectKey");
        String login = getPropertyForTest("sonar.base.login");
        String pwd = getPropertyForTest("sonar.base.password");

        Properties config = new Properties();
        config.setProperty("front.page.logo", logo);
        config.setProperty("sonar.base.url", sonarUrl);

        ResourceBundle rb = ResourceBundle.getBundle(PDFResources.RESOURCE_NAME, Locale.getDefault(),
                this.getClass().getClassLoader());
        Properties configText = new Properties();
        Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            configText.setProperty(key, (String) rb.getObject(key));
        }

        Credentials credentials = new Credentials(sonarUrl, login, pwd);

        PDFReporter reporter = new TeamWorkbookPDFReporter(credentials, this.getClass().getResource("/" + logo),
                projectKey, config, configText);

        ByteArrayOutputStream baos = reporter.getReport();
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream("target/testReport.pdf");
            baos.writeTo(fos);
            fos.flush();

        } catch (IOException e) {
            assert(false);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    assert(false);
                }
            }
        }

    }

}