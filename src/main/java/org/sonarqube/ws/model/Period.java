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

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;


/** 
* @author : liujiey
* @date 创建时间：2018年11月15日 上午11:00:05 
* @param
* @throws  
* @return   Period
*/
public class Period implements Model{

	private static final long serialVersionUID = 2996255578584211763L;
	private Integer index;
	private String value;
	@CheckForNull
	public String getValue() {
		return value;
	}
	public void setValue(@Nullable String value) {
		this.value = value;
	}
	@CheckForNull
	public Integer getIndex() {
		return index;
	}
	public void setIndex(@Nullable Integer index) {
		this.index = index;
	}
}