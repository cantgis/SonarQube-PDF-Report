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

/**
 * 
 *
 */
import java.awt.Color;
import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;


/**
 * Table of contents
 *
 */
public class Toc extends PdfPageEventHelper {

    private static final Logger LOG = LoggerFactory.getLogger(Events.class);
    private Document tocDocument;
    private ByteArrayOutputStream tocOutputStream;
    private PdfPTable content;


    public Toc() {

        tocDocument = new Document(PageSize.A4, 50, 50, 110, 50);
        content = new PdfPTable(2);
        Rectangle page = tocDocument.getPageSize();
		
        content.setTotalWidth(page.getWidth() - tocDocument.leftMargin() - tocDocument.rightMargin());
        content.getDefaultCell().setUseVariableBorders(true);
        content.getDefaultCell().setBorderColorBottom(Color.WHITE);
        content.getDefaultCell().setBorderColorRight(Color.WHITE);
        content.getDefaultCell().setBorderColorLeft(Color.WHITE);
        content.getDefaultCell().setBorderColorTop(Color.WHITE);
        content.getDefaultCell().setBorderWidthBottom(2f);
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onChapter(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document, float, com.lowagie.text.Paragraph)
     */
    @Override
    public void onChapter(final PdfWriter writer, final Document document, final float position,
            final Paragraph title) {
        content.getDefaultCell().setBorderColorBottom(Color.LIGHT_GRAY);
        content.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
        content.getDefaultCell().setUseBorderPadding(true);
        content.addCell(new Phrase(title.getContent(), Style.NORMAL_FONT));
        content.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
        content.addCell(new Phrase("Page " + document.getPageNumber(), new Font(Font.HELVETICA, 11)));
        content.getDefaultCell().setBorderColorBottom(Color.WHITE);
        content.getDefaultCell().setUseBorderPadding(false);
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onChapterEnd(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document, float)
     */
    @Override
    public void onChapterEnd(final PdfWriter writer, final Document document, final float position) {
        content.addCell("");
        content.addCell("");
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onSection(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document, float, int, com.lowagie.text.Paragraph)
     */
    @Override
    public void onSection(final PdfWriter writer, final Document document, final float position, final int depth,
            final Paragraph title) {
        content.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
        if (depth == 2) {
            content.getDefaultCell().setIndent(10);
            content.addCell(new Phrase(title.getContent(), Style.CONTENTS_FONT));
            content.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
            content.addCell(new Phrase(Integer.toString(document.getPageNumber()), new Font(Font.HELVETICA, 10)));
        } else {
            content.getDefaultCell().setIndent(20);
            content.addCell(new Phrase(title.getContent(), Style.CONTENTS_FONT));
            content.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
            content.addCell(new Phrase(Integer.toString(document.getPageNumber()), new Font(Font.HELVETICA, 10)));
        }
        content.getDefaultCell().setIndent(0);
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onCloseDocument(com.lowagie.text.pdf.PdfWriter,
     *      com.lowagie.text.Document)
     */
    @Override
    public void onCloseDocument(final PdfWriter writer, final Document document) {
        try {
            tocDocument.add(content);
        } catch (DocumentException e) {
            LOG.error("Can not add TOC", e);
        }
    }

    public Document getTocDocument() {
        return tocDocument;
    }

    public ByteArrayOutputStream getTocOutputStream() {
        return tocOutputStream;
    }

    public void setHeader(final Header header) {
        tocOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(tocDocument, tocOutputStream);
            writer.setPageEvent(header);
        } catch (DocumentException e) {
            LOG.error("Cannot add TOC", e);
        }
    }
}
