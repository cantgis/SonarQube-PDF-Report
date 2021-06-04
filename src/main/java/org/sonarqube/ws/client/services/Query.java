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
package org.sonarqube.ws.client.services;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.sonarqube.ws.internal.EncodingUtils;
import org.sonarqube.ws.model.Model;

/**
 * Abstract class for queries
 *
 * @param <M>
 */
public abstract class Query<M extends Model> extends AbstractQuery<M> {

    /**
     * 
     */
    private static final long serialVersionUID = -1553725545034067021L;
    public static final int MAX_PAGE_SIZE = 500;

    /**
     * @return class for the Model
     */
    public abstract Class<M> getModelClass();

    /**
     * @see org.sonarqube.ws.client.services.AbstractQuery#getUrl()
     */
    @Override
    public String getUrl() {
        StringBuilder url = new StringBuilder(getBaseUrl());
        url.append('?');
        for (Map.Entry<String, Serializable> entry : getParams().entrySet()) {
            appendUrlParameter(url, entry.getKey(), entry.getValue());
        }
        return url.toString();
    }

    /**
     * @return base URL
     */
    public abstract String getBaseUrl();

    /**
     * @return parameters Map
     */
    public abstract Map<String, Serializable> getParams();

    /**
     * Add a parameter array to the map
     * 
     * @param key
     *            key
     * @param values
     *            values
     * @return Query
     */
    protected Query<M> addParam(String key, String[] values) {
        if (values != null) {
            getParams().put(key, EncodingUtils.toQueryParam(values));
        }
        return this;
    }

    /**
     * Add a parameter to the map
     * 
     * @param key
     *            key
     * @param value
     *            value
     * @return Query
     */
    protected Query<M> addParam(String key, String value) {
        return addParam(key, new String[] { value });
    }

    /**
     * Add a boolean parameter to the map
     * 
     * @param key
     *            key
     * @param value
     *            value
     * @return Query
     */
    protected Query<M> addParam(String key, Boolean value) {
        getParams().put(key, value);
        return this;
    }

    /**
     * Add a Date parameter to the map
     * 
     * @param key
     *            key
     * @param value
     *            value
     * @return Query
     */
    protected Query<M> addParam(String key, Date d) {
        getParams().put(key, EncodingUtils.toQueryParam(d, true));
        return this;
    }

    /**
     * Add a integer parameter to the map
     * 
     * @param key
     *            key
     * @param value
     *            value
     * @return Query
     */
    protected Query<M> addParam(String key, Integer value) {
        getParams().put(key, value);
        return this;
    }

}