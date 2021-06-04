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


import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.Plugin;
import org.sonar.report.pdf.PDFResources;
import org.sonar.report.pdf.batch.PDFPostJob;

/**
 * Report Plugin main class
 *
 */
@Properties({
        @Property(key = PDFPostJob.SKIP_PDF_KEY, name = "Skip", description = "Skip generation of PDF report.", defaultValue = ""
                + PDFPostJob.SKIP_PDF_DEFAULT_VALUE, global = true, project = true, module = false, type = PropertyType.BOOLEAN),
        @Property(key = PDFPostJob.REPORT_TYPE, name = "Type", description = "Report type.", defaultValue = PDFPostJob.REPORT_TYPE_DEFAULT_VALUE, global = true, project = true, module = false, type = PropertyType.SINGLE_SELECT_LIST, options = {
                PDFResources.EXECUTIVE_REPORT_TYPE, PDFResources.WORKBOOK_REPORT_TYPE }),
        @Property(key = PDFPostJob.USERNAME, name = "Username", description = "Username for WS API access.", defaultValue = PDFPostJob.USERNAME_DEFAULT_VALUE, global = true, project = true, module = false),
        @Property(key = PDFPostJob.SONAR_P_KEY, name = "Password", description = "Password for WS API access.", defaultValue = PDFPostJob.SONAR_P_DEFAULT_VALUE, global = true, project = true, module = false, type = PropertyType.PASSWORD) })
public class PDFReportPlugin implements Plugin {
   
	@Override
	public void define(Context context) {
		context.addExtensions(PDFPostJob.class, PDFReportPageDefinition.class,PDFWebservice.class);
    }		
	}

