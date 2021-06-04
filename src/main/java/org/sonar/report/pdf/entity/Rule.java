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
 * Bean defining the rules
 *
 */
public class Rule implements Entity {

    /**
     * 
     */
    private static final long serialVersionUID = -2345517439556616421L;

    // Rule key
    private String key;

    // Rule name
    private String name;

    // Rule description
    private String htmlDesc;

    // Severity
    private Severity severity;

    // Violations of this rule: <resource key, violation line> (with limit 100)
    private List<Violation> topViolatedResources;

    // Total violations of this rule
    private String violationsNumber;

    private String message;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return htmlDesc;
    }

    public List<Violation> getTopViolations() {
        return topViolatedResources;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setDescription(final String htmlDesc) {
        this.htmlDesc = htmlDesc;
    }

    public void setTopViolations(final List<Violation> violations) {
        this.topViolatedResources = violations;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getViolationsNumber() {
        return violationsNumber;
    }

    public void setViolationsNumber(final String violationsNumber) {
        this.violationsNumber = violationsNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }



}
