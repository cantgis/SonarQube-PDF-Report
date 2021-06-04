/*
 * SonarQube PDF Report Copyright (C) 2010-2017 SonarSource SA mailto:info AT
 * sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.sonar.report.pdf.batch;

import java.io.File;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.report.pdf.PDFResources;
import org.sonar.report.pdf.util.FileUploader;

/**
 * Extension point for PDF Job
 *
 */

public class PDFPostJob implements PostJob
{

    public static final String PROJECT_KEY = "sonar.projectKey";
    public static final String PROJECT_NAME = "sonar.projectName";
    public static final String SKIP_PDF_KEY = "sonar.pdf.skip";
    public static final boolean SKIP_PDF_DEFAULT_VALUE = false;
    public static final String REPORT_TYPE = "report.type";
    public static final String REPORT_TYPE_DEFAULT_VALUE = PDFResources.WORKBOOK_REPORT_TYPE;
    public static final String USERNAME = "sonar.pdf.username";
    public static final String USERNAME_DEFAULT_VALUE = "";
    public static final String SONAR_P_KEY = "sonar.pdf.password";
    public static final String SONAR_P_DEFAULT_VALUE = "";
    public static final String SONAR_HOST_URL = "sonar.host.url";
    public static final String SONAR_HOST_URL_DEFAULT_VALUE = "http://localhost:9001";
    private static final String PDF_EXTENSION = ".pdf";
    private static final Logger LOG = LoggerFactory.getLogger(PDFPostJob.class);
    private final Settings settings;
    private final FileSystem fs;

    public PDFPostJob(Settings settings, FileSystem fs) {
        this.settings = settings;
        this.fs = fs;

    }

    public boolean shouldExecuteOnProject() {
        return settings.hasKey(SKIP_PDF_KEY) ? !settings.getBoolean(SKIP_PDF_KEY) : !SKIP_PDF_DEFAULT_VALUE;
    }

    @Override
    public void describe(PostJobDescriptor descriptor) {
        descriptor.name("PDF Report");

    }

    @Override
    public void execute(PostJobContext context) {

        if (!shouldExecuteOnProject()) {
            LOG.info("SKIP_PDF_KEY is true! Skip generating PDF Report!");
            return;
        }

        LOG.info("Executing decorator: PDF Report");

        String sonarHostUrl = settings.hasKey(SONAR_HOST_URL) ? settings.getString(SONAR_HOST_URL)
                : SONAR_HOST_URL_DEFAULT_VALUE;
        String username = settings.hasKey(USERNAME) ? settings.getString(USERNAME) : USERNAME_DEFAULT_VALUE;
        String password = settings.hasKey(SONAR_P_KEY) ? settings.getString(SONAR_P_KEY) : SONAR_P_DEFAULT_VALUE;
        String reportType = settings.hasKey(REPORT_TYPE) ? settings.getString(REPORT_TYPE) : REPORT_TYPE_DEFAULT_VALUE;
        String projectkey = "";
        Optional<String> result = context.config().get(PROJECT_KEY);
        if (result.isPresent()) {
            projectkey = result.get();
        }
		if (context.config().hasKey("sonar.branch")) {
			projectkey = projectkey + ":" + context.config().get("sonar.branch");
		}
        try {
            PDFGenerator generator = new PDFGenerator(projectkey, fs, sonarHostUrl, username, password, reportType);
            generator.execute();
        }
        catch (Exception e) {
            LOG.error("Problem generating PDF file.", e);
        }
        String path = fs.workDir().getAbsolutePath() + "/" + projectkey.replace(':', '-').replace('/', '-')
                + PDF_EXTENSION;
        File pdf = new File(path);
        if (pdf.exists()) {
            FileUploader.upload(pdf, sonarHostUrl + PDFResources.PDF_REPORT_STORE_PATH, username, password);
        }
        else {
            LOG.error("PDF file not found in local filesystem. Report could not be sent to server.");
        }
    }

}
