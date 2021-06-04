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
import org.sonarqube.ws.model.TimeMachines;

/**
 * Query for time machine
 *
 */
public class TimeMachineQuery extends Query<TimeMachines> {
    /**
     * 
     */
    private static final long serialVersionUID = 9152911963151770915L;
    private final Map<String, Serializable> params = new HashMap<>();
    private static final String BASE_URL = "/api/timemachine";

    private TimeMachineQuery() {
    }

    @Override
    public Class<TimeMachines> getModelClass() {
        return TimeMachines.class;
    }

    public static TimeMachineQuery create() {
        return new TimeMachineQuery();
    }

    /**
     * URL query string, for internal use
     */
    public Map<String, Serializable> urlParams() {
        return params;
    }

    public TimeMachineQuery metrics(String... s) {
        return (TimeMachineQuery) addParam("metrics", s);
    }

    public TimeMachineQuery resource(String... s) {
        return (TimeMachineQuery) addParam("resource", s);
    }

    public TimeMachineQuery fromDateTime(Date d) {
        return (TimeMachineQuery) addParam("fromDateTime", d);
    }

    public TimeMachineQuery toDateTime(Date d) {
        return (TimeMachineQuery) addParam("toDateTime", d);
    }

    public TimeMachineQuery format(String format) {
        return (TimeMachineQuery) addParam("format", format);
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
