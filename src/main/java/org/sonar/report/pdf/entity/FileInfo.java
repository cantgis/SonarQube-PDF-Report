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

/**
 * Bean for file information
 *
 */
public class FileInfo implements Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 4793538491240285526L;

    /**
     * Sonar resource key.
     */
    private String key;

    /**
     * Resource name (filename).
     */
    private String name;

    /**
     * Number of violations ins this resource (file).
     */
    private String violations;

    /**
     * Class complexity.
     */
    private String complexity;

    /**
     * Duplicated lines in this resource (file)
     */
    private String duplicatedLines;

    /**
     * It defines the content of this object: used for violations info,
     * complexity info or duplications info.
     */

    public boolean isContentSet(final FileInfoTypes content) {
        boolean result = false;
        if (content == FileInfoTypes.VIOLATIONS_CONTENT) {
            result = !("0").equals(this.getViolations());
        } else if (content == FileInfoTypes.CCN_CONTENT) {
            result = !("0").equals(this.getComplexity());
        } else if (content == FileInfoTypes.DUPLICATIONS_CONTENT) {
            result = !("0").equals(this.getDuplicatedLines());
        }
        return result;
    }

    public String getKey() {
        return key;
    }

    public String getViolations() {
        return violations;
    }

    public String getComplexity() {
        return complexity;
    }

    public String getName() {
        return name;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setViolations(final String violations) {
        this.violations = violations;
    }

    public void setComplexity(final String complexity) {
        this.complexity = complexity;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDuplicatedLines() {
        return duplicatedLines;
    }

    public void setDuplicatedLines(final String duplicatedLines) {
        this.duplicatedLines = duplicatedLines;
    }

}
