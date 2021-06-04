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
package org.sonarqube.ws.connectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.sonarqube.ws.client.SonarHost;
import org.sonarqube.ws.client.services.AbstractQuery;
import org.sonarqube.ws.client.services.Query;

/**
 * Implementation of Connector with Apache HttpClient
 *
 */
public class HttpClient3Connector implements Connector {
    /**
     * 
     */
    private static final long serialVersionUID = 4395780993756345105L;
    private static final int MAX_TOTAL_CONNECTIONS = 40;
    private static final int MAX_HOST_CONNECTIONS = 4;

    private final SonarHost server;
    private transient HttpClient httpClient;

    public HttpClient3Connector(final SonarHost server) {
        this.server = server;
        this.createClient();
    }

    private void createClient() {
        final HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setConnectionTimeout(AbstractQuery.DEFAULT_TIMEOUT_MILLISECONDS);
        params.setSoTimeout(AbstractQuery.DEFAULT_TIMEOUT_MILLISECONDS);
        params.setDefaultMaxConnectionsPerHost(MAX_HOST_CONNECTIONS);
        params.setMaxTotalConnections(MAX_TOTAL_CONNECTIONS);
        final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.setParams(params);
        this.httpClient = new HttpClient(connectionManager);
        configureCredentials();
    }

    private void configureCredentials() {
        if (server.getUsername() != null) {
            httpClient.getParams().setAuthenticationPreemptive(true);
            Credentials defaultcreds = new UsernamePasswordCredentials(server.getUsername(), server.getPassword());
            httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);
        }
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public String execute(Query<?> query) throws ConnectionException {
        return executeRequest(newGetRequest(query));
    }

    /**
     * Reauest execution
     * 
     * @param method
     *            method
     * @return String result
     */
    private String executeRequest(HttpMethodBase method) throws ConnectionException {
        String json = null;
        try {
            httpClient.executeMethod(method);

            if (method.getStatusCode() == HttpStatus.SC_OK) {
                json = getResponseBodyAsString(method);

            } else if (method.getStatusCode() != HttpStatus.SC_NOT_FOUND) {
                throw new ConnectionException("HTTP error: " + method.getStatusCode() + ", msg: "
                        + method.getStatusText() + ", query: " + method);
            }

        } catch (IOException e) {
            throw new ConnectionException("Query: " + method, e);

        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return json;
    }

    private HttpMethodBase newGetRequest(Query<?> query) {
        HttpMethodBase method = new GetMethod(server.getHost() + query.getUrl());
        initRequest(method, query);
        return method;
    }

    private void initRequest(HttpMethodBase request, AbstractQuery<?> query) {
        request.setRequestHeader("Accept", "application/json");
        if (query.getLocale() != null) {
            request.setRequestHeader("Accept-Language", query.getLocale());
        }
        request.getParams().setSoTimeout(query.getTimeoutMilliseconds());
    }

    private String getResponseBodyAsString(HttpMethod method) throws ConnectionException {

        try (InputStream inputStream = method.getResponseBodyAsStream();
                BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"));) {

            final StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();

        } catch (IOException e) {
            throw new ConnectionException("Can not read response", e);

        }
    }
}
