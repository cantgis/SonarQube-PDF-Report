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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.report.pdf.entity.Project;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * Header for report
 */
public class Header extends PdfPageEventHelper {

    private static final Logger LOG = LoggerFactory.getLogger(Header.class);

    private URL logo;
    private Project project;

    public Header(final URL logo, final Project project) {
        this.logo = logo;
        this.project = project;
    }

    /**
     * 
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document)
     */
    @Override
    public void onEndPage(final PdfWriter writer, final Document document) {
        try {
            Image logoImage = Image.getInstance(logo);
            Rectangle page = document.getPageSize();
            PdfPTable head = new PdfPTable(4);
            head.getDefaultCell().setVerticalAlignment(PdfCell.ALIGN_MIDDLE);
            head.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
            head.addCell(logoImage);
            Phrase projectName = new Phrase(project.getName(),Style.HEAD_FONT);

            Phrase phrase = new Phrase(PDFResources.SONAR_PDF_REPORT,
                    FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL, Color.GRAY));
            head.getDefaultCell().setColspan(2);
			head.addCell(projectName);
            head.getDefaultCell().setColspan(1);
			head.addCell(phrase);
            head.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
            head.writeSelectedRows(0, -1, document.leftMargin(), page.getHeight() - 20, writer.getDirectContent());
            head.setSpacingAfter(10);
        } catch (BadElementException | IOException e) {
            LOG.error("Can not generate PDF header", e);
        }
    }

}
