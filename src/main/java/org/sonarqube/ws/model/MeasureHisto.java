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
package org.sonarqube.ws.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * MeasureHisto model
 *
 */
public class MeasureHisto implements Model, Comparable<MeasureHisto> {
    /**
     * 
     */
    private static final long serialVersionUID = -6000002419307005691L;
    @SerializedName("d")
    private Date date;
    @SerializedName("v")
    private List<String> values = new ArrayList<>();

    public Date getDate() {
        return (Date) date.clone();
    }

    public void setDate(Date date) {
        if (date != null) {
            this.date = (Date) date.clone();
        }
    }

    public List<String> getValues() {
        return values;
    }

    public String getValue() {
        return values.get(0);
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MeasureHisto) {
            return getDate().equals(((MeasureHisto) obj).getDate());
        } else {
            return false;
        }

    }

    @Override
    public int compareTo(MeasureHisto o) {
        return getDate().compareTo(o.getDate());
    }

}
