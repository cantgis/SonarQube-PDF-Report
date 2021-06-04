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

import java.util.Iterator;
import java.util.List;

import org.sonar.report.pdf.util.MetricKeys;

/**
 * This class encapsulates the Project info.
 */
public class Project implements Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 1330596249644859216L;
    // Project info
    private short id;
    private String key;
    private String name;
    private String description;
    private Integer fixedissues;
    private Integer removedissues;

    // Measures
    private Measures measures;

    // Child projects
    private List<Project> subprojects;
    
    // Most violated rules
    private List<Rule> mostViolatedRuleslist;

    // Most violated rules
    private List<Rule> mostViolatedRules;
    
    // Qprofile rules
    private List<Qprofile> qprofile;

    // Most complex elements
    private List<FileInfo> mostComplexFiles;

    // Most violated files
    private List<FileInfo> mostViolatedFiles;

    // Most duplicated files
    private List<FileInfo> mostDuplicatedFiles;

    public Project(final String key) {
        this.key = key;
    }

    public Measure getMeasure(final String measureKey) {
        if (measures.containsMeasure(measureKey)) {
            return measures.getMeasure(measureKey);
        } else {
            return new Measure(null, EntityUtils.NA_METRICS.getKey());
        }
    }

    public Measure getMeasure(final MetricKeys measureKey) {
        return getMeasure(measureKey.getKey());
    }

    public Project getChildByKey(final String key) {
        Iterator<Project> it = this.subprojects.iterator();
        while (it.hasNext()) {
            Project child = it.next();
            if (child.getKey().equals(key)) {
                return child;
            }
        }
        return null;
    }

    public void setId(final short id) {
        this.id = id;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setName(final String name) {
        this.name = name;
    }
    
    public void setFixedissues(final Integer fixedissues) {
        this.fixedissues = fixedissues;
    }
    
    public void setRemovedissues(final Integer removedissues) {
        this.removedissues = removedissues;
    }
    
    public Integer getFixedissues() {
    	return fixedissues;
    }
    
    public Integer getRemovedissues() {
    	return removedissues;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public short getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Project> getSubprojects() {
        return subprojects;
    }

    public void setSubprojects(final List<Project> subprojects) {
        this.subprojects = subprojects;
    }

    public Measures getMeasures() {
        return measures;
    }

    public void setMeasures(final Measures measures) {
        this.measures = measures;
    }

    public List<Rule> getMostViolatedRules() {
        return mostViolatedRules;
    }
    
    
    
    public List<Qprofile> getQprofile() {
        return qprofile;
    }

    public List<FileInfo> getMostViolatedFiles() {
        return mostViolatedFiles;
    }


    
    public void setMostViolatedRules(final List<Rule> mostViolatedRules) {
        this.mostViolatedRules = mostViolatedRules;
    }

    public void setMostViolatedFiles(final List<FileInfo> mostViolatedFiles) {
        this.mostViolatedFiles = mostViolatedFiles;
    }

    public void setMostComplexFiles(final List<FileInfo> mostComplexFiles) {
        this.mostComplexFiles = mostComplexFiles;
    }

    public List<FileInfo> getMostComplexFiles() {
        return mostComplexFiles;
    }

    public List<FileInfo> getMostDuplicatedFiles() {
        return mostDuplicatedFiles;
    }

    public void setMostDuplicatedFiles(final List<FileInfo> mostDuplicatedFiles) {
        this.mostDuplicatedFiles = mostDuplicatedFiles;
    }

	public void setQprofile(final List<Qprofile> qprofile) {
		 this.qprofile = qprofile;
		
	}

	public List<Rule> getMostViolatedRuleslist() {
		return mostViolatedRuleslist;
	}

	public void setMostViolatedRuleslist(List<Rule> mostViolatedRuleslist) {
		this.mostViolatedRuleslist = mostViolatedRuleslist;
	}
}
