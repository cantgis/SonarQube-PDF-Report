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
package org.sonar.report.pdf.entity;

import java.util.List;

/**
 * Bean for file information
 *
 */
public class Qprofile implements Entity {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = -2598450467709857305L;




    private String key;


    private String name;


    private String language;
    
    private List<org.sonarqube.ws.model.Rule> qprofileRules;

   
    public String getKey() {
        return key;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<org.sonarqube.ws.model.Rule> getQprofileRules() {
        return qprofileRules;
    }
    
    public void setQprofileRules(final List<org.sonarqube.ws.model.Rule> qprofileRules) {
		 this.qprofileRules = qprofileRules;
		
	}

}
