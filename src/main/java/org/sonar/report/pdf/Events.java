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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Add the logo header to the PDF document.
 */
public class Events extends PdfPageEventHelper {

    private static final Logger LOG = LoggerFactory.getLogger(Events.class);

    private Toc toc;
    private Header header;

    /**
     * Constructor
     * 
     * @param toc
     *            toc
     * @param header
     *            header
     */
    public Events(final Toc toc, final Header header) {
        this.toc = toc;
        this.header = header;
        toc.setHeader(header);
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onChapter(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document, float, com.lowagie.text.Paragraph)
     */
    @Override
    public void onChapter(final PdfWriter writer, final Document document, final float position,
            final Paragraph paragraph) {
        toc.onChapter(writer, document, position, paragraph);
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onChapterEnd(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document, float)
     */
    @Override
    public void onChapterEnd(final PdfWriter writer, final Document document, final float position) {
        toc.onChapterEnd(writer, document, position);
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onSection(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document, float, int, com.lowagie.text.Paragraph)
     */
    @Override
    public void onSection(final PdfWriter writer, final Document document, final float position, final int depth,
            final Paragraph paragraph) {
        toc.onSection(writer, document, position, depth, paragraph);
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document)
     */
    @Override
    public void onEndPage(final PdfWriter writer, final Document document) {
        header.onEndPage(writer, document);
        printPageNumber(writer, document);
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onCloseDocument(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document)
     */
    @Override
    public void onCloseDocument(final PdfWriter writer, final Document document) {
        toc.onCloseDocument(writer, document);
    }

    /**
     * Print page number
     * 
     * @param writer
     *            writer
     * @param document
     *            document
     */
    private void printPageNumber(final PdfWriter writer, final Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        float textBase = document.bottom() - 20;
        try {
            cb.setFontAndSize(BaseFont.createFont("Helvetica", BaseFont.WINANSI, false), 12);
        } catch (DocumentException | IOException e) {
            LOG.error("Can not print page number", e);
        }
        cb.beginText();
        cb.setTextMatrix(document.right() - 10, textBase);
        cb.showText(String.valueOf(writer.getPageNumber()));
        cb.endText();
        cb.saveState();
    }
}
