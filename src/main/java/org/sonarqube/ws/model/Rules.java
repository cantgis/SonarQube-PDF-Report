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
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Rules model
 *
 */
public class Rules implements Model {


    private static final long serialVersionUID = -2053999490614642772L;
    @SerializedName("rules")
    private final List<Rule> rulesList = new ArrayList<>();
    private final List<Component> actives = new ArrayList<>();
    private final List<User> facets = new ArrayList<>();
    private Paging paging;
    private int total;
    private int p;
    private int ps;

    public Paging getPaging() {
        return paging;
    }
    public int getTotal() {
        return total;
    }
    public int getP() {
        return p;
    }
    public int getPs() {
        return ps;
    }
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<Rule> getRules() {
        return rulesList;
    }

    public List<Component> getActives() {
        return actives;
    }

    public List<User> getFacets() {
        return facets;
    }
}
