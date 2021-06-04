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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckForNull;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.sonarqube.ws.client.services.WSUtils;
import org.sonarqube.ws.client.unmarshallers.JsonUtils;

/**
 * JDK Utils
 *
 */
public class JdkUtils extends WSUtils {

    @Override
    public String format(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    @Override
    public String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Encoding not supported", e);
        }
    }

    @Override
    public Object getField(Object json, String field) {
        return ((JSONObject) json).get(field);
    }

    @Override
    @CheckForNull
    public String getString(Object json, String field) {
        return JsonUtils.getString((JSONObject) json, field);
    }

    @Override
    @CheckForNull
    public Boolean getBoolean(Object json, String field) {
        return JsonUtils.getBoolean((JSONObject) json, field);
    }

    @Override
    @CheckForNull
    public Integer getInteger(Object json, String field) {
        return JsonUtils.getInteger((JSONObject) json, field);
    }

    @Override
    @CheckForNull
    public Double getDouble(Object json, String field) {
        return JsonUtils.getDouble((JSONObject) json, field);
    }

    @Override
    @CheckForNull
    public Long getLong(Object json, String field) {
        return JsonUtils.getLong((JSONObject) json, field);
    }

    @Override
    @CheckForNull
    public Date getDateTime(Object json, String field) {
        return JsonUtils.getDateTime((JSONObject) json, field);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int getArraySize(Object array) {
        return ((ArrayList) array).size();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getArrayElement(Object array, int i) {
        return ((ArrayList) array).get(i);
    }

    @Override
    public Object parse(String jsonStr) {
        return JSONValue.parse(jsonStr);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getFields(Object json) {
        return ((JSONObject) json).keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> getFieldsWithValues(Object json) {
        JSONObject jsonMap = (JSONObject) json;
        Map<String, String> mapFromSet = new HashMap<>();
        for (String entry : (Set<String>) jsonMap.keySet()) {
            mapFromSet.put(entry, (String) jsonMap.get(entry));
        }
        return mapFromSet;
    }
}
