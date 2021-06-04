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
package org.sonar.report.pdf;

import java.io.Serializable;

/**
 * Static resources
 *
 */
public final class PDFResources implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5843975448490825719L;

    public static final String RESOURCE_NAME = "report-texts";
    public static final String TENDENCY_DIR = "/tendency/";
    public static final String FRONT_PAGE_LOGO = "front.page.logo";
    public static final String SONAR_PNG_FILE = "/sonar.png";
	public static final String CHINESE_FONT_FILE = "/msyh.ttf";
    public static final String SONAR_BASE_URL = "sonar.base.url";

    public static final String REPORT_PROPERTIES = "report.properties";

    public static final String WORKBOOK_REPORT_TYPE = "workbook";

    public static final String EXECUTIVE_REPORT_TYPE = "executive";

    public static final String HTTP_PATTERN = "http://";

    public static final String SONAR_PDF_REPORT = "Sonar Report";

    public static final String MAIN_TABLE_OF_CONTENTS = "main.table.of.contents";

    public static final String GENERAL_NO_VIOLATED_FILES = "general.noViolatedFiles";

    public static final String GENERAL_MOST_VIOLATED_FILES = "general.mostViolatedFiles";

    public static final String GENERAL_NO_VIOLATED_RULES = "general.noViolatedRules";

    public static final String GENERAL_MOST_VIOLATED_RULES = "general.mostViolatedRules";

    public static final String GENERAL_NO_COMPLEX_FILES = "general.noComplexFiles";

    public static final String GENERAL_MOST_COMPLEX_FILES = "general.mostComplexFiles";

    public static final String GENERAL_NO_DUPLICATED_FILES = "general.noDuplicatedFiles";

    public static final String GENERAL_MOST_DUPLICATED_FILES = "general.mostDuplicatedFiles";

    public static final String GENERAL_VIOLATIONS = "general.violations";
	public static final String GENERAL_NEW_VIOLATIONS = "general.newviolations";
	public static final String GENERAL_BUGS = "general.bugs";
	public static final String GENERAL_SECURITY = "general.vulnerabilities";
	public static final String GENERAL_MAINTAINABILITY = "general.codesmells";

    public static final String GENERAL_BLOCKER_VIOLATIONS = "general.blockerViolations";

    public static final String GENERAL_CRITICAL_VIOLATIONS = "general.criticalViolations";

    public static final String GENERAL_MAJOR_VIOLATIONS = "general.majorViolations";
    
    public static final String GENERAL_MINOR_VIOLATIONS = "general.minorViolations";
    
    public static final String GENERAL_INFO_VIOLATIONS = "general.infoViolations";

    public static final String GENERAL_TECHNICAL_DEBT = "general.technicalDebt";
    public static final String GENERAL_OPEN_VIOLATIONS = "general.openViolations";
    public static final String GENERAL_REOPENED_VIOLATIONS = "general.reopenedViolations";
    public static final String GENERAL_CONFIRMED_VIOLATIONS = "general.confirmededViolations";
    public static final String GENERAL_FALSE_POSITIVE_VIOLATIONS = "general.falsePositiveViolations";
    public static final String GENERAL_WONT_FIX_VIOLATIONS = "general.wontFixViolations";
    public static final String GENERAL_FIXED_VIOLATIONS = "general.fixedViolations";
    public static final String GENERAL_REMOVED_VIOLATIONS = "general.removedViolations";
	
	public static final String GENERAL_RELIABILITY_REMEDIATION_EFFORT = "general.ReliabilityRemediationEffort";	
	
	public static final String GENERAL_SECUTITY_REMEDIATION_EFFORT = "general.SecurityRemediationEffort";

    public static final String GENERAL_CODING_RULES_VIOLATIONS = "general.codingRulesViolations";

    public static final String GENERAL_ERRORS = "general.errors";

    public static final String GENERAL_FAILURES = "general.failures";
    
    public static final String GENERAL_SKIPS = "general.skips";

    public static final String GENERAL_TEST_SUCCESS = "general.testSuccess";

    public static final String GENERAL_TESTS = "general.tests";

    public static final String GENERAL_BRANCHCOVERAGE = "general.branchcoverage";

    public static final String GENERAL_CODE_COVERAGE = "general.codeCoverage";
    
    public static final String GENERAL_LINE_COVERAGE = "general.linecoverage";
    

    public static final String GENERAL_DYNAMIC_ANALYSIS = "general.dynamicAnalysis";

    public static final String GENERAL_DECISION_POINTS = "general.decisionPoints";

    public static final String GENERAL_PER_CLASS = "general.perClass";

    public static final String GENERAL_COMPLEXITY = "general.complexity";

    public static final String GENERAL_COMMENT_LINES = "general.commentLines";

    public static final String GENERAL_COMMENTS = "general.comments";

    public static final String GENERAL_DUPLICATED_LINES = "general.duplicatedLines";

    public static final String GENERAL_METHODS = "general.methods";
    public static final String GENERAL_SIZE = "general.size";

    public static final String GENERAL_CLASSES = "general.classes";
    public static final String GENERAL_FILES = "general.files";
    public static final String GENERAL_PACKAGES = "general.packages";
    public static final String GENERAL_LINES = "general.lines";

    public static final String GENERAL_LINES_OF_CODE = "general.linesOfCode";

    public static final String GENERAL_STATIC_ANALYSIS = "general.staticAnalysis";

    public static final String GENERAL_VIOLATIONS_DASHBOARD = "general.violationsDashboard";

    public static final String GENERAL_VIOLATIONS_ANALYSIS = "general.violationsAnalysis";

    public static final String GENERAL_REPORT_OVERVIEW = "general.reportOverview";

    public static final String GENERAL_VIOLATIONS_DETAILS = "general.violationsDetails";
    public static final String GENERAL_QPROFILES = "general.qprofiles";

    public static final String GENERAL_LINE = "general.line";
    public static final String GENERAL_FILE = "general.file";
    public static final String GENERAL_RULE = "general.rule";
    public static final String GENERAL_RULE_DESC = "general.ruledesc";
    public static final String GENERAL_VIOLATIONS_TYPE = "general.violationsType";
    public static final String GENERAL_VIOLATIONS_SERVERITY = "general.violationsSeverity";

    public static final String MAIN_TEXT_MISC_OVERVIEW = "main.text.misc.overview";
    public static final String MAIN_TEXT_MISC_OVERVIEW1 = "main.text.misc.overview1";
    public static final String MAIN_TEXT_MISC_OVERVIEW2 = "main.text.misc.overview2";
    public static final String MAIN_TEXT_MISC_OVERVIEW3 = "main.text.misc.overview3";
    public static final String MAIN_TEXT_MISC_OVERVIEW4 = "main.text.misc.overview4";
    public static final String MAIN_TEXT_MISC_OVERVIEW5 = "main.text.misc.overview5";

    public static final String METRICS_CCN_CLASSES_COUNT_DISTRIBUTION = "metrics.ccnClassesCountDistribution";

    public static final String FILE_SCOPE = "FIL";

    public static final String PROJECT_SCOPE = "PRJ";

    public static final String SONAR_DETAILS_LIMIT = "sonar.details.limit";

    public static final String SONAR_TABLE_LIMIT = "sonar.table.limit";

    public static final String PDF_REPORT_STORE_PATH = "/api/pdfreport/store";

    private PDFResources() {
        super();
    }

}
