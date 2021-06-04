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
import java.util.Date;

import javax.annotation.CheckForNull;

public class Task implements Model {


	private static final long serialVersionUID = -7152919429726036686L;
	private String id;
	private String type;
	private String componentId;
	private String componentKey;
	private String componentQualifier;
	private String analysisId;
	private String status;
	private Date submittedAt;
	private String submitterLogin;
	private Date startedAt;
	private Date executedAt;
	private Integer executionTimeMs;
	private Boolean logs;

	@CheckForNull
	public String getId() {
		return id;
	}
	
	@CheckForNull
	public String getType() {
		return type;
	}
	
	@CheckForNull
	public String getcomponentId() {
		return componentId;
	}
	
	@CheckForNull
	public String getcomponentKey() {
		return componentKey;
	}
	
	public String getcomponentQualifier() {
		return componentQualifier;
	}
	
	
	public String getanalysisId() {
		return analysisId;
	}
	
	
	public String getsubmitterLogin() {
		return submitterLogin;
	}
	
	public String getstatus() {
		return status;
	}
	
	public Integer getexecutionTimeMs() {
		return executionTimeMs;
	}
	
	public Boolean getlogs() {
		return logs;
	}

    public Date getsubmittedAt() {
        return (Date) submittedAt.clone();
    }
    public Date getexecutedAt() {
        return (Date) executedAt.clone();
    }
    public Date getstartedAt() {
        return (Date) startedAt.clone();
    }
    
}
