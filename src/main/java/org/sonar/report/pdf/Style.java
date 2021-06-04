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
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.BaseFont;


/**
 * Static Styles for PDF reporting
 *
 */
public class Style {

    /**
     * Font used in main chapters title
     */

	private static final Logger LOG = LoggerFactory.getLogger(Style.class);

	public static final BaseFont CHINESE = setfont();

    public static final Font CHAPTER_FONT = new Font(CHINESE, 18, Font.BOLD, Color.GRAY);

    /**
     * Font used in sub-chapters title
     */
    public static final Font TITLE_FONT = new Font(CHINESE, 14, Font.BOLD, Color.GRAY);

	/**
     * Font used in TABLE_OF_CONTENTS
     */
	public static final Font CONTENTS_FONT = new Font(CHINESE, 9,Font.NORMAL, Color.BLACK);

    /**
     * Font used in HEAD
     */
	public static final Font HEAD_FONT = new Font(CHINESE, 12,Font.NORMAL, Color.GRAY);
    /**
     * Font used in graphics foots
     */
    public static final Font FOOT_FONT = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, Color.GRAY);

    /**
     * Font used in general plain text
     */
    public static final Font NORMAL_FONT = new Font(CHINESE, 11, Font.NORMAL, Color.BLACK);

    /**
     * Font used in code text (bold)
     */
    public static final Font MONOSPACED_BOLD_FONT = new Font(Font.COURIER, 11, Font.BOLD, Color.BLACK);

    /**
     * Font used in code text
     */
    public static final Font MONOSPACED_FONT = new Font(Font.COURIER, 10, Font.NORMAL, Color.BLACK);

    /**
     * Font used in table of contents title
     */
    public static final Font TOC_TITLE_FONT = new Font(CHINESE, 24, Font.BOLD, Color.GRAY);

    /**
     * Font used in front page (Project name)
     */
    public static final Font FRONTPAGE_FONT_1 = new Font(CHINESE, 22, Font.BOLD, Color.BLACK);

    /**
     * Font used in front page (Project description)
     */
    public static final Font FRONTPAGE_FONT_2 = new Font(Font.HELVETICA, 18, Font.ITALIC, Color.BLACK);

    /**
     * Font used in front page (Project date)
     */
    public static final Font FRONTPAGE_FONT_3 = new Font(Font.HELVETICA, 16, Font.BOLDITALIC, Color.GRAY);

    /**
     * Underlined font
     */
    public static final Font UNDERLINED_FONT = new Font(CHINESE, 14, Font.UNDERLINE, Color.BLACK);

    /**
     * Dashboard metric title font
     */
    public static final Font DASHBOARD_TITLE_FONT = new Font(CHINESE, 12, Font.NORMAL, Color.BLACK);

    /**
     * Dashboard metric value font
     */
    public static final Font DASHBOARD_DATA_FONT = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.GRAY);

    /**
     * Dashboard metric details font
     */
    public static final Font DASHBOARD_DATA_FONT_2 = new Font(CHINESE, 10, Font.NORMAL,
            new Color(100, 150, 190));

    /**
     * Tendency icons height + 2 (used in tables style)
     */
    public static final int TENDENCY_ICONS_HEIGHT = 20;

    public static final float FRONTPAGE_LOGO_POSITION_X = 114;

    public static final float FRONTPAGE_LOGO_POSITION_Y = 542;

    private Style() {
        super();
    }

	public static BaseFont setfont() {
		try{
		return BaseFont.createFont(PDFResources.CHINESE_FONT_FILE, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 
		}catch(Exception e){
			 LOG.error("Can not generate yaheiChinese Font", e);
			 return null;
		}
    }


    public static void noBorderTable(final PdfPTable table) {
        table.getDefaultCell().setBorderColor(Color.WHITE);
    }
    
    public static void alignCenterTable(final PdfPTable table) {
    	 table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    }
    
    public static void alignRightTable(final PdfPTable table) {
   	 table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
   }


    /**
     * This method makes a simple table with content.
     * 
     * @param left
     *            Data for left column
     * @param right
     *            Data for right column
     * @param title
     *            The table title
     * @param noData
     *            Showed when left or right are empty
     * @return The table (iText table) ready to add to the document
     */
    public static PdfPTable createSimpleTable(final List<String> left, final List<String> right, final String title,
            final String noData) {
        return createSimpleTable(left, right, null, title, noData);
    }

    /**
     * This method makes a simple table with content.
     * 
     * @param left
     *            Data for left column
     * @param right
     *            Data for right column
     * @param title
     *            The table title
     * @param noData
     *            Showed when left or right are empty
     * @return The table (iText table) ready to add to the document
     */
    public static PdfPTable createSimpleTable(final List<String> left, final List<String> right,
            final List<Color> colors, final String title, final String noData) {
        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setColspan(2);
        table.addCell(new Phrase(title, Style.DASHBOARD_TITLE_FONT));
        table.getDefaultCell().setBackgroundColor(Color.GRAY);
        table.addCell("");
        table.getDefaultCell().setColspan(1);
        table.getDefaultCell().setBackgroundColor(Color.WHITE);

        Iterator<String> itLeft = left.iterator();
        Iterator<String> itRight = right.iterator();
        Iterator<Color> itColor = null;
        boolean isColorSet = false;
        if (colors != null) {
            itColor = colors.iterator();
            isColorSet = true;
        }

        while (itLeft.hasNext()) {
            String textLeft = itLeft.next();
            String textRight = itRight.next();
            PdfPCell leftCell = new PdfPCell(new Phrase(textLeft,Style.NORMAL_FONT));
            PdfPCell rightCell = new PdfPCell(new Phrase(textRight,Style.NORMAL_FONT));
            if (isColorSet) {
                Color color = itColor.next();
                leftCell.setBackgroundColor(color);
                rightCell.setBackgroundColor(color);
            }
            table.addCell(leftCell);
            table.addCell(rightCell);
        }

        if (left.isEmpty()) {
            table.getDefaultCell().setColspan(2);
            table.addCell(noData);
        }

        table.setSpacingBefore(20);
        table.setSpacingAfter(20);

        return table;
    }

    /**
     * Method for creating a table with 2 columns
     * 
     * @param titles
     *            titles
     * @param content
     *            content
     * @return The table (iText table) ready to add to the document
     */
    public static PdfPTable createTwoColumnsTitledTable(final List<String> titles, final List<String> content) {
        PdfPTable table = new PdfPTable(10);
        Iterator<String> itLeft = titles.iterator();
        Iterator<String> itRight = content.iterator();
        while (itLeft.hasNext()) {
            String textLeft = itLeft.next();
            String textRight = itRight.next();
            table.getDefaultCell().setColspan(1);
            table.addCell(textLeft);
            table.getDefaultCell().setColspan(9);
            table.addCell(textRight);
        }
        table.setSpacingBefore(20);
        table.setSpacingAfter(20);
        table.setLockedWidth(false);
        table.setWidthPercentage(90);
        return table;
    }
}
