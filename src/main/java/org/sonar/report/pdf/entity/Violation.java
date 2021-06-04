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

import org.sonar.report.pdf.util.MetricKeys;

/**
 * Violation bean
 *
 */
public class Violation implements Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 2050348920551647807L;
    private String resource;
    private String line;

    public Violation(final String line, final String resource) {
        this.line = line;
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public String getLine() {
        return line;
    }

    public void setResource(final String resource) {
        this.resource = resource;
    }

    public void setLine(final String line) {
        this.line = line;
    }

    public static MetricKeys getViolationLevelByKey(final String level) {
        MetricKeys violationLevel = null;
        if (level.equals(Severity.INFO.getValue())) {
            violationLevel = MetricKeys.INFO_VIOLATIONS;
        } else if (level.equals(Severity.MINOR.getValue())) {
            violationLevel = MetricKeys.MINOR_VIOLATIONS;
        } else if (level.equals(Severity.MAJOR.getValue())) {
            violationLevel = MetricKeys.MAJOR_VIOLATIONS;
        } else if (level.equals(Severity.CRITICAL.getValue())) {
            violationLevel = MetricKeys.CRITICAL_VIOLATIONS;
        } else if (level.equals(Severity.BLOCKER.getValue())) {
            violationLevel = MetricKeys.BLOCKER_VIOLATIONS;
        }
        return violationLevel;
    }

}
