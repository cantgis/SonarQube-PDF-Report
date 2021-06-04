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
package org.sonarqube.ws.client;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.sonar.report.pdf.entity.exception.ReportException;
import org.sonarqube.ws.client.services.Query;
import org.sonarqube.ws.client.services.WSUtils;
import org.sonarqube.ws.client.unmarshallers.ListOfJson;
import org.sonarqube.ws.client.unmarshallers.UnmarshalException;
import org.sonarqube.ws.connectors.Connector;
import org.sonarqube.ws.connectors.ConnectorFactory;
import org.sonarqube.ws.model.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Sonar Webservice client
 *
 */
public class WSClient implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -515443204212628343L;
    private static Gson gson;

    static {
        WSUtils.setInstance(new JdkUtils());
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    }

    private Connector connector;

    public WSClient(Connector connector) {
        this.connector = connector;
    }

    public Connector getConnector() {
        return connector;
    }

    /**
     * Find request
     * 
     * @param query
     *            query
     * @return Model
     */
    public <M extends Model> M find(Query<M> query) throws ReportException {
        String json = connector.execute(query);
        M result = null;
        if (json != null) {
            try {
                result = gson.fromJson(json, query.getModelClass());
            } catch (JsonSyntaxException e) {
                throw new UnmarshalException(query, json, e);
            }
        }
        return result;
    }

    /**
     * Find all request
     * 
     * @param query
     *            query
     * @return List of Model
     */
    public <M extends Model> List<M> findAll(Query<M> query) throws ReportException {
        String json = connector.execute(query);
        List<M> result;
        if (json == null) {
            result = Collections.emptyList();
        } else {
            try {
                result = gson.fromJson(json, new ListOfJson<M>(query.getModelClass()));
            } catch (Exception e) {
                throw new UnmarshalException(query, json, e);
            }
        }
        return result;
    }

    /**
     * Create a client
     * 
     * @param host
     *            host
     * @return WSClient
     */
    public static WSClient create(String host) {
        return create(host, null, null);
    }

    /**
     * Create a client
     * 
     * @param host
     *            host
     * @param username
     *            username
     * @param password
     *            password
     * @return WSClient
     */
    public static WSClient create(String host, String username, String password) {
        return new WSClient(ConnectorFactory.create(new SonarHost(host, username, password)));
    }

}
