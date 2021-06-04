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

/**
 * Bean defining measure
 *
 */
public class Measure implements Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 1506882233935063179L;
    private String key;
    private String value;
    private String formatValue;
    private String dataValue;

    public Measure(final String measureKey, final String measureFValue) {
        this.key = measureKey;
        this.formatValue = measureFValue;
    }

    public Measure() {
        super();
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getFormatValue() {
        return formatValue;
    }

    public void setFormatValue(final String formatValue) {
        this.formatValue = formatValue;
    }

   

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(final String dataValue) {
        this.dataValue = dataValue;
    }

}