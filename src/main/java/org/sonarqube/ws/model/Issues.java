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
package org.sonarqube.ws.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sonarqube.ws.model.Facet.ValueBean;

import com.google.gson.annotations.SerializedName;

/**
 * Issues model
 *
 */
public class Issues implements Model {
    /**
     * 
     */
    private static final long serialVersionUID = 3032289626485456483L;

    @SerializedName("issues")
    private final List<Issue> issuesList = new ArrayList<>();

    private final List<Component> components = new ArrayList<>();
    private final List<Rule> rules = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Facet> facets = new ArrayList<>();
    private Paging paging;

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<Issue> getIssues() {
        return issuesList;
    }
    
    public List<Facet> getFacets() {
        return facets;
    }

    public List<ValueBean> getRuleFacet() {
    	
    	if (this.facets.isEmpty()){
    		return Collections.emptyList();          
    	}
		for(int i = 0;i < this.facets.size();i++){
    		if (facets.get(i).getProperty().equalsIgnoreCase("rules")) {
    			return facets.get(i).getValues();
    		}
    	}
		return Collections.emptyList();
    }
    
    public List<Component> getComponents() {
        return components;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<User> getUsers() {
        return users;
    }

}
