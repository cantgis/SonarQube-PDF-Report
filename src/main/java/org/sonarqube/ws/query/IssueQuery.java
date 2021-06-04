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
package org.sonarqube.ws.query;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.sonarqube.ws.client.services.Query;
import org.sonarqube.ws.model.Issues;

/**
 * Query for issues
 *
 */
public class IssueQuery extends Query<Issues> {
    /**
     * 
     */
    private static final long serialVersionUID = 8282477750415566385L;
    private final Map<String, Serializable> params = new HashMap<>();
    private static final String BASE_URL = "/api/issues/search";

    private IssueQuery() {
    }

    @Override
    public Class<Issues> getModelClass() {
        return Issues.class;
    }

    public static IssueQuery create() {
        return new IssueQuery();
    }

    public IssueQuery actionPlans(String... s) {
        return (IssueQuery) addParam("actionPlans", s);
    }

    public IssueQuery additionalFields(String... s) {
        return (IssueQuery) addParam("additionalFields", s);
    }

    public IssueQuery asc(boolean asc) {
        return (IssueQuery) addParam("asc", asc);
    }

    public IssueQuery assigned(Boolean assigned) {
        return (IssueQuery) addParam("assigned", assigned);
    }

    public IssueQuery assignees(String... s) {
        return (IssueQuery) addParam("assignees", s);
    }

    public IssueQuery authors(String... s) {
        return (IssueQuery) addParam("authors", s);
    }

    public IssueQuery componentKeys(String... s) {
        return (IssueQuery) addParam("componentKeys", s);
    }

    public IssueQuery createdAt(Date d) {
        return (IssueQuery) addParam("createdAt", d);
    }

    public IssueQuery createdAfter(Date d) {
        return (IssueQuery) addParam("createdAfter", d);
    }

    public IssueQuery createdBefore(Date d) {
        return (IssueQuery) addParam("createdBefore", d);
    }

    public IssueQuery createdInLast(String range) {
        return (IssueQuery) addParam("createdInLast", range);
    }

    public IssueQuery directories(String... keys) {
        return (IssueQuery) addParam("directories", keys);
    }


    public IssueQuery facets(String... s) {
        return (IssueQuery) addParam("facets", s);
    }

    public IssueQuery fileUuids(String... s) {
        return (IssueQuery) addParam("fileUuids", s);
    }

    public IssueQuery issues(String... keys) {
        return (IssueQuery) addParam("issues", keys);
    }

    public IssueQuery languages(String... s) {
        return (IssueQuery) addParam("languages", s);
    }

    public IssueQuery moduleUuids(String... s) {
        return (IssueQuery) addParam("moduleUuids", s);
    }

    public IssueQuery onComponentOnly(boolean onComponentOnly) {
        return (IssueQuery) addParam("planned", onComponentOnly);
    }

    public IssueQuery pageIndex(int pageIndex) {
        return (IssueQuery) addParam("p", pageIndex);
    }

    public IssueQuery planned(Boolean planned) {
        return (IssueQuery) addParam("planned", planned);
    }

    public IssueQuery projectKeys(String... s) {
        return (IssueQuery) addParam("projectKeys", s);
    }

    public IssueQuery projectUuids(String... s) {
        return (IssueQuery) addParam("projectUuids", s);
    }

    public IssueQuery pageSize(int pageSize) {
        return (IssueQuery) addParam("ps", pageSize);
    }

    public IssueQuery reporters(String... s) {
        return (IssueQuery) addParam("reporters", s);
    }

    public IssueQuery resolutions(String... resolutions) {
        return (IssueQuery) addParam("resolutions", resolutions);
    }

    public IssueQuery resolved(Boolean resolved) {
        return (IssueQuery) addParam("resolved", resolved);
    }

    public IssueQuery rules(String... s) {
        return (IssueQuery) addParam("rules", s);
    }

    public IssueQuery sort(String sort) {
        return (IssueQuery) addParam("s", sort);
    }

    public IssueQuery severities(String... severities) {
        return (IssueQuery) addParam("severities", severities);
    }

    public IssueQuery statuses(String... statuses) {
        return (IssueQuery) addParam("statuses", statuses);
    }
    

    public IssueQuery tags(String... tags) {
        return (IssueQuery) addParam("tags", tags);
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public Map<String, Serializable> getParams() {
        return params;
    }

}
