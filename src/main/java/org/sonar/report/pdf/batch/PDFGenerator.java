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
package org.sonar.report.pdf.batch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;

import org.sonar.report.pdf.ExecutivePDFReporter;
import org.sonar.report.pdf.PDFReporter;
import org.sonar.report.pdf.PDFResources;
import org.sonar.report.pdf.TeamWorkbookPDFReporter;
import org.sonar.report.pdf.entity.exception.ReportException;
import org.sonar.report.pdf.util.Credentials;

/**
 * PDF Generator
 *
 */
public class PDFGenerator {

    private static final String REPORT_PROPERTIES = "/report.properties";

    private static final Logger LOG = LoggerFactory.getLogger(PDFGenerator.class);

    private String sonarHostUrl;
    private String username;
    private String password;
    private String reportType;

    private String project;
    private FileSystem fs;

    public PDFGenerator(final String projectkey, final FileSystem fs, final String sonarHostUrl, final String username,
            final String password, final String reportType) {
        this.project = projectkey;
        this.fs = fs;
        this.sonarHostUrl = sonarHostUrl;
        this.username = username;
        this.password = password;
        this.reportType = reportType;
    }

    /**
     * Main method : execution of the reporting
     */
    public void execute() {
        Properties config = new Properties();
        Properties configLang = new Properties();

        try (InputStream configStream = this.getClass().getResourceAsStream(REPORT_PROPERTIES)) {
            if (sonarHostUrl != null) {
                if (sonarHostUrl.endsWith("/")) {
                    sonarHostUrl = sonarHostUrl.substring(0, sonarHostUrl.length() - 1);
                }
                config.put(PDFResources.SONAR_BASE_URL, sonarHostUrl);
                config.put(PDFResources.FRONT_PAGE_LOGO, "sonar.png");
            } else {
                config.load(configStream);
            }

            ResourceBundle rb = ResourceBundle.getBundle(PDFResources.RESOURCE_NAME, Locale.getDefault(),
                    this.getClass().getClassLoader());

            Enumeration<String> keys = rb.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                configLang.setProperty(key, (String) rb.getObject(key));
            }

            Credentials credentials = new Credentials(config.getProperty(PDFResources.SONAR_BASE_URL), username,
                    password);

            String sonarProjectId = project;
            String path = fs.workDir().getAbsolutePath() + "/" + sonarProjectId.replace(':', '-').replace('/', '-') + ".pdf";

            PDFReporter reporter = null;
            if (reportType != null && (PDFResources.EXECUTIVE_REPORT_TYPE).equals(reportType)) {
                LOG.info("Executive report type selected");
                reporter = new ExecutivePDFReporter(credentials,
                        this.getClass().getResource(PDFResources.SONAR_PNG_FILE), sonarProjectId, config, configLang);
            } else {
                LOG.info("Team workbook report type selected");
                reporter = new TeamWorkbookPDFReporter(credentials,
                        this.getClass().getResource(PDFResources.SONAR_PNG_FILE), sonarProjectId, config, configLang);
            }
            ByteArrayOutputStream baos = reporter.getReport();
            FileOutputStream fos = new FileOutputStream(new File(path));
            baos.writeTo(fos);
            fos.flush();
            fos.close();
            String e = sonarProjectId.replace(':', '-');
            LOG.info("PDF report generated (see {}.pdf on build output directory)",e);
        } catch (ReportException | IOException e) {
            LOG.error("Problem generating PDF file.", e);
        }
    }

}
