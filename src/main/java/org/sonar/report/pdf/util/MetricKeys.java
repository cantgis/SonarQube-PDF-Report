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
package org.sonar.report.pdf.util;

/**
 * Enum for usable metric keys
 *
 */
public enum MetricKeys {

	PROFILE("quality_profiles"), DUPLICATED_LINES("duplicated_lines"), DUPLICATED_BLOCKS(
			"duplicated_blocks"), DUPLICATED_FILES("duplicated_files"), CLASSES("classes"), COMMENT_LINES(
					"comment_lines"), COMPLEXITY("complexity"), FUNCTIONS("functions"), NCLOC("ncloc"),LINES("lines"), DIRECTORIES(
							"directories"), FILES("files"),COVERAGE("coverage"), LINE_COVERAGE("line_coverage"), BRANCH_COVERAGE("branch_coverage"),TEST_EXECUTION_TIME(
									"test_execution_time"), SKIPPED_TESTS("skipped_tests"), TESTS("tests"), TEST_ERRORS(
											"test_errors"), TEST_FAILURES("test_failures"), TEST_SUCCESS_DENSITY(
													"test_success_density"), VIOLATIONS(
															"violations"), SECURITY(
																	"vulnerabilities"),BUGS(
																			"bugs"),MAINTAINABILITY(
																					"code_smells"),FILE_COMPLEXITY_DISTRIBUTION(
																							"file_complexity_distribution"), DUPLICATED_LINES_DENSITY(
																									"duplicated_lines_density"), CLASS_COMPLEXITY(
																											"class_complexity"),FILE_COMPLEXITY(
																													"file_complexity"), FUNCTION_COMPLEXITY(
																															"function_complexity"), COMMENT_LINES_DENSITY(
																																	"comment_lines_density"), TECHNICAL_DEBT(
																																			"sqale_index"), RELIABILITY_REMEDIATION_EFFORT
	("reliability_remediation_effort"),SECUTITY_REMEDIATION_EFFORT
	("security_remediation_effort"),BLOCKER_VIOLATIONS(
			"blocker_violations"), CRITICAL_VIOLATIONS(
					"critical_violations"), MAJOR_VIOLATIONS(
							"major_violations"), MINOR_VIOLATIONS(
									"minor_violations"),OPEN_ISSUES(
											"open_issues"),REOPENED_ISSUES(
													"reopened_issues"),CONFIRMED_ISSUES(
															"confirmed_issues"),FALSE_POSITIVE_ISSUES(
																	"false_positive_issues"),WONT_FIX_ISSUES(
																			"wont_fix_issues"), INFO_VIOLATIONS(
											"info_violations");

	private final String key;

	MetricKeys(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static boolean isMetricNeeded(String key) {
		for (MetricKeys metric : values()) {
			if (metric.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
}
