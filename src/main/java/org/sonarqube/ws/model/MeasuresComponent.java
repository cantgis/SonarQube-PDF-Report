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
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public class MeasuresComponent implements Model {


	private static final long serialVersionUID = -7152919429726036686L;
	private String id;
	private String key;
	private String name;
	private String qualifier;
	private String description;
	private List<ComponentMeasure> measures;
	@CheckForNull
	public String getId() {
		return id;
	}

	@CheckForNull
	public String getKey() {
		return key;
	}
	
	@CheckForNull
	public String getName() {
		return name;
	}

	
	@CheckForNull
	public String getQualifier() {
		return qualifier;
	}
	
	public String getDescription() {
		return description;
	}

	public List<ComponentMeasure> getMeasures() {
		return measures;
	}

	public void setMeasures(@Nullable List<ComponentMeasure> measures) {
		this.measures = measures;
	}
	

}
