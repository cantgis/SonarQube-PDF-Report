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
package org.sonar.report.pdf.entity;

import java.awt.Color;
import java.util.Arrays;

import org.sonar.report.pdf.entity.exception.ReportException;

/**
 * Bean defining severities.
 */
public enum Severity {
    INFO("INFO", 5, new Color(51, 255, 51)), MINOR("MINOR", 4, new Color(153, 255, 51)), MAJOR("MAJOR", 3,
            new Color(255, 255, 51)), CRITICAL("CRITICAL", 2, new Color(255, 153, 51)), BLOCKER("BLOCKER", 1,
                    new Color(255, 51, 51));

    private final Integer intValue;
    private final String value;
    private final Color color;

    private Severity(String value, Integer intValue, Color color) {
        this.intValue = intValue;
        this.value = value;
        this.color = color;
        SeverityLookup.lookup.put(value, this);
    }

    public Color getColor() {
        return color;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public String getValue() {
        return value;
    }

    private static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
    }

    public static String[] getSeverityArray() {
        return getNames(Severity.class);
    }

    public static Severity get(String value) throws ReportException {
        Severity severity = SeverityLookup.lookup.get(value);
        if (severity == null) {
            severity=INFO;
            //throw new ReportException("Cannnot find serverity for key " + value);
        }
        return severity;
    }
}
