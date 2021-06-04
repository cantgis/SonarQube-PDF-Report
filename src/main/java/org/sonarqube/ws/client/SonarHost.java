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

import java.io.Serializable;

/**
 * Bean defining the Sonar Host
 *
 */
public class SonarHost implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7479172149694486308L;
    private String host;
    private String username;
    private String password;

    public SonarHost(String host) {
        this.host = host;
    }

    public SonarHost(String host, String username, String password) {
        this(host);
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public SonarHost setHost(String host) {
        this.host = host;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SonarHost setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SonarHost setPassword(String password) {
        this.password = password;
        return this;
    }
}
