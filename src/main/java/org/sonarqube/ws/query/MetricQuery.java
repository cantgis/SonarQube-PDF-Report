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

import org.sonarqube.ws.client.services.Query;
import org.sonarqube.ws.model.Metrics;

/**
 * Query for metrics
 *
 */
public final class MetricQuery extends Query<Metrics> {
    /**
     * 
     */
    private static final long serialVersionUID = -5121355570153816079L;
    private final Map<String, Serializable> params = new HashMap<>();
    public static final String BASE_URL = "/api/metrics/search";

    private MetricQuery() {
    }

    @Override
    public Class<Metrics> getModelClass() {
        return Metrics.class;
    }

    public static MetricQuery all() {
        return new MetricQuery().pageSize(MAX_PAGE_SIZE);
    }

    public MetricQuery pageSize(int pageSize) {
        return (MetricQuery) addParam("ps", pageSize);
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