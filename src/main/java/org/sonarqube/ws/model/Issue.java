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
import java.util.List;

/**
 * Issue model
 *
 */
public class Issue implements Model {
    /**
     * 
     */
    private static final long serialVersionUID = -1752907370620124719L;
    private String key;
    private String component;
    private String project;
    private String rule;
    private String status;
    private String resolution;
    private String severity;
    private String message;
    private Integer line;
    private TextRange textRange;
    private String author;
    private String effort;
	private String type;
    private Date creationDate;
    private Date updateDate;
    private List<String> tags;
    private List<Comment> comments;
    private Attribute attr;
    private List<String> transitions;
    private List<String> actions;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
	 public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public TextRange getTextRange() {
        return textRange;
    }

    public void setTextRange(TextRange textRange) {
        this.textRange = textRange;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDebt() {
        return effort;
    }

    public void setDebt(String effort) {
        this.effort = effort;
    }

    public Date getCreationDate() {
        return (Date) creationDate.clone();
    }

    public void setCreationDate(Date creationDate) {
        if (creationDate != null) {
            this.creationDate = (Date) creationDate.clone();
        }
    }

    public Date getUpdateDate() {
        return (Date) updateDate.clone();
    }

    public void setUpdateDate(Date updateDate) {
        if (updateDate != null) {
            this.updateDate = (Date) updateDate.clone();
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Attribute getAttr() {
        return attr;
    }

    public void setAttr(Attribute attr) {
        this.attr = attr;
    }

    public List<String> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<String> transitions) {
        this.transitions = transitions;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

}
