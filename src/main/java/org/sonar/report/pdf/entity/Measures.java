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


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class encapsulates the measures info.
 */
public class Measures implements Entity {

    /**
     * 
     */
    private static final long serialVersionUID = -9169792843537741693L;
    private Map<String, Measure> measuresTable = new HashMap<>();
    private Date date;
    private String version = EntityUtils.NA_METRICS.getKey();

    public Measures() {
        super();
    }

    public Date getDate() {
        return (Date) date.clone();
    }

    public void setDate(final Date date) {
        if (date != null) {
            this.date = (Date) date.clone();
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public int getMeasuresCount() {
        return measuresTable.size();
    }

    public Set<String> getMeasuresKeys() {
        return measuresTable.keySet();
    }

    public Measure getMeasure(final String key) {
        return measuresTable.get(key);
    }

    public void addMeasure(final String name, final Measure value) {
        measuresTable.put(name, value);
    }

    public boolean containsMeasure(final String measureKey) {
        return measuresTable.containsKey(measureKey);
    }

}
