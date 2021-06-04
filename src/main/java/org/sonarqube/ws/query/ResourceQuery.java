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
import java.util.HashMap;
import java.util.Map;

import org.sonar.report.pdf.util.MetricKeys;
import org.sonarqube.ws.client.services.Query;
import org.sonarqube.ws.model.Resource;

/**
 * Query for resources
 *
 */
public class ResourceQuery extends Query<Resource> {

    /**
     * 
     */
    private static final long serialVersionUID = 7718983822095052052L;
    private final Map<String, Serializable> params = new HashMap<>();
    public static final String BASE_URL = "/api/resources";

    public static final int DEPTH_UNLIMITED = -1;

    public ResourceQuery() {
        super();
    }

    public ResourceQuery(String resourceKeyOrId) {
        this.setResourceKeyOrId(resourceKeyOrId);
    }

    public ResourceQuery setDepth(Integer depth) {
        return (ResourceQuery) addParam("depth", depth);
    }

    public ResourceQuery setAllDepths() {
        return setDepth(DEPTH_UNLIMITED);
    }

    public ResourceQuery setResourceKeyOrId(String resourceKeyOrId) {
        return (ResourceQuery) addParam("resource", resourceKeyOrId);
    }

    public ResourceQuery setResourceId(int resourceId) {
        return (ResourceQuery) addParam("resource", resourceId);
    }

    public ResourceQuery setLimit(Integer limit) {
        return (ResourceQuery) addParam("limit", limit);
    }

    public ResourceQuery setScopes(String... scopes) {
        return (ResourceQuery) addParam("scopes", scopes);
    }

    public ResourceQuery setQualifiers(String... qualifiers) {
        return (ResourceQuery) addParam("qualifiers", qualifiers);
    }

    public ResourceQuery setMetrics(String... metrics) {
        return (ResourceQuery) addParam("metrics", metrics);
    }

    public ResourceQuery setRules(String... rules) {
        return (ResourceQuery) addParam("rules", rules);
    }

    public ResourceQuery setVerbose(Boolean verbose) {
        return (ResourceQuery) addParam("verbose", verbose);
    }

    public ResourceQuery setIncludeTrends(Boolean includeTrends) {
        return (ResourceQuery) addParam("includetrends", includeTrends);
    }

    public ResourceQuery setIncludeAlerts(Boolean includeAlerts) {
        return (ResourceQuery) addParam("includealerts", includeAlerts);
    }

    public ResourceQuery setFormat(String format) {
        return (ResourceQuery) addParam("format", format);
    }

    @Override
    public final Class<Resource> getModelClass() {
        return Resource.class;
    }

    public static ResourceQuery createForMetrics(String resourceKeyOrId, String... metricKeys) {
        return new ResourceQuery(resourceKeyOrId).setMetrics(metricKeys).setVerbose(true).setFormat(JSON_FORMAT);
    }

    public static ResourceQuery createForMetrics(String resourceKeyOrId, MetricKeys metricKey) {
        return createForMetrics(resourceKeyOrId, metricKey.getKey());
    }

    public static ResourceQuery createForResource(Resource resource, String... metricKeys) {
        Integer id = resource.getId();
        if (id == null) {
            throw new IllegalArgumentException("id must be set");
        }
        return new ResourceQuery(id.toString()).setMetrics(metricKeys);
    }

    public static ResourceQuery create(String resourceKey) {
        return new ResourceQuery(resourceKey);
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