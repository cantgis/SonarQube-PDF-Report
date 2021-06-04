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

import javax.annotation.Nullable;

import org.sonarqube.ws.model.Model;

/**
 * Abstract Query for a Model
 *
 * @param <M>
 */
public abstract class AbstractQuery<M extends Model> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6759723766395108354L;

    private static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";

    private static final String LONG_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final String JSON_FORMAT = "json";

    public static final int DEFAULT_TIMEOUT_MILLISECONDS = 180 * 1000;

    private int timeoutMilliseconds = DEFAULT_TIMEOUT_MILLISECONDS;

    // accepted-language as defined in
    // http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
    private String locale;

    /**
     * Must start with a slash, for example: /api/metrics
     * <p>
     * IMPORTANT: In implementations of this method we must use helper methods
     * to construct URL.
     * </p>
     *
     * @see #encode(String)
     * @see #appendUrlParameter(StringBuilder, String, Object)
     * @see #appendUrlParameter(StringBuilder, String, Object[])
     * @see #appendUrlParameter(StringBuilder, String, Date, boolean)
     */
    public abstract String getUrl();

    /**
     * Request body. By default it is empty but it can be overridden.
     */
    public String getBody() {
        return null;
    }

    /**
     * Get the timeout for waiting data, in milliseconds. A value of zero is
     * interpreted as an infinite timeout.
     */
    public final int getTimeoutMilliseconds() {
        return timeoutMilliseconds;
    }

    /**
     * Set the timeout for waiting data, in milliseconds. Avalue of zero is
     * interpreted as an infinite timeout.
     */
    public final AbstractQuery<M> setTimeoutMilliseconds(int i) {
        this.timeoutMilliseconds = i < 0 ? 0 : i;
        return this;
    }

    /**
     * Accepted-language, as defined in
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
     */
    public final String getLocale() {
        return locale;
    }

    /**
     * Set the Accepted-language HTTP parameter
     */
    public final AbstractQuery<M> setLocale(String locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Encodes single parameter value.
     */
    protected static String encode(String value) {
        return WSUtils.getInstance().encodeUrl(value);
    }

    /**
     * Append URL parameter for integer parameter
     * 
     * @param url
     *            URL
     * @param paramKey
     *            paramKey
     * @param paramValue
     *            paramValue
     */
    protected static void appendUrlParameter(StringBuilder url, String paramKey, int paramValue) {
        url.append(paramKey).append('=').append(paramValue).append("&");
    }

    /**
     * Append URL parameter for object
     * 
     * @param url
     *            URL
     * @param paramKey
     *            paramKey
     * @param paramValue
     *            paramValue
     */
    protected static void appendUrlParameter(StringBuilder url, String paramKey, @Nullable Object paramValue) {
        if (paramValue != null) {
            url.append(paramKey).append('=').append(encode(paramValue.toString())).append('&');
        }
    }

    /**
     * Append URL parameter for objects
     * 
     * @param url
     *            URL
     * @param paramKey
     *            paramKey
     * @param paramValues
     *            paramValues
     */
    protected static void appendUrlParameter(StringBuilder url, String paramKey, @Nullable Object[] paramValues) {
        if (paramValues != null) {
            url.append(paramKey).append('=');
            for (int index = 0; index < paramValues.length; index++) {
                if (index > 0) {
                    url.append(',');
                }
                if (paramValues[index] != null) {
                    url.append(encode(paramValues[index].toString()));
                }
            }
            url.append('&');
        }
    }

    /**
     * Append URL parameter for Date
     * 
     * @param url
     *            URL
     * @param paramKey
     *            paramKey
     * @param paramValue
     *            paramValue
     * @param includeTime
     *            includeTime
     */
    protected static void appendUrlParameter(StringBuilder url, String paramKey, @Nullable Date paramValue,
            boolean includeTime) {
        if (paramValue != null) {
            String format = includeTime ? LONG_DATE_FORMAT : SHORT_DATE_FORMAT;
            url.append(paramKey).append('=').append(encode(WSUtils.getInstance().format(paramValue, format)))
                    .append('&');
        }
    }
}