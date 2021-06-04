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
import org.sonarqube.ws.model.MeasuresComponents;


public class MeasuresComponentQuery extends Query<MeasuresComponents>  {

	private static final long serialVersionUID = 6601793563912618737L;
	private final Map<String, Serializable> params = new HashMap<>();
	public static final String BASE_URL = "/api/measures/component";

	
	
	public MeasuresComponentQuery() {
		super();
	}

	public MeasuresComponentQuery(String componentKey) {
		this.setComponentKey(componentKey);
	}

	public MeasuresComponentQuery setComponentId(String componentId) {
		 return (MeasuresComponentQuery) addParam("componentId", componentId);
	}


	public MeasuresComponentQuery setComponentKey(String componentKey) {
		  return (MeasuresComponentQuery) addParam("component", componentKey);
	}


	public MeasuresComponentQuery setMetrics(String... metrics) {
		 return (MeasuresComponentQuery) addParam("metricKeys", metrics);
	}



	@Override
	public Class<MeasuresComponents> getModelClass() {
		return MeasuresComponents.class;
	}

	public static MeasuresComponentQuery createForMetrics(String componentKey,String... metricKeys) {
		return new MeasuresComponentQuery(componentKey).setMetrics(metricKeys);
	}

	public static MeasuresComponentQuery createForMetrics(String componentKey, MetricKeys metricKey) {
	        return createForMetrics(componentKey, metricKey.getKey());
	    }

	public static MeasuresComponentQuery create(String componentKey) {
	        return new MeasuresComponentQuery(componentKey);
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
