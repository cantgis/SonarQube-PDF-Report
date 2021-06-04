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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Facet  implements Model{


	private static final long serialVersionUID = -146117028189686874L;
	private String property;
	 private List<ValueBean> values = new ArrayList<>();
	 
	 public static class ValueBean implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3857042771905909413L;
			private String val;
	        private Integer count;

	        public String getval() {
	            return val;
	        }
	        public Integer getcount() {
	            return count;
	        }
	 }
	public String getProperty() {
		return property;
	}
	public List<ValueBean> getValues() {
		return values;
	}

}
