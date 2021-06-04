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
import org.sonarqube.ws.model.Analyses;


public class ProjectAnalysesQuery extends Query<Analyses>  {

	private static final long serialVersionUID = -8590364986681708012L;
	private final Map<String, Serializable> params = new HashMap<>();
	public static final String BASE_URL = "/api/project_analyses/search";

	
	
	public ProjectAnalysesQuery() {
		super();
	}

	public ProjectAnalysesQuery(String projectkey) {
		this.setProjectKey(projectkey);
	}

	public ProjectAnalysesQuery setProjectKey(String projectkey) {
		 return (ProjectAnalysesQuery) addParam("project", projectkey);
	}


	public ProjectAnalysesQuery setcategory(String category) {
		  return (ProjectAnalysesQuery) addParam("category", category);
	}
	

	public ProjectAnalysesQuery setps(int ps) {
		  return (ProjectAnalysesQuery) addParam("ps", ps);
	}
	
	public ProjectAnalysesQuery setp(int p) {
		  return (ProjectAnalysesQuery) addParam("p", p);
	}
	@Override
	public Class<Analyses> getModelClass() {
		return Analyses.class;
	}

	
	public static ProjectAnalysesQuery create(String projectkey) {
	        return new ProjectAnalysesQuery(projectkey);
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
