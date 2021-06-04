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
package org.sonar.report.pdf.test;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public abstract class SonarPDFTest {
    protected Properties configTest;

    SonarPDFTest() {
        super();
        URL resourceText = this.getClass().getClassLoader().getResource("test.properties");
        if (resourceText == null) {
            System.out.println("\nProblem loading test.propeties.");
        } else {
            configTest = new Properties();
            try {
                configTest.load(resourceText.openStream());
            } catch (IOException e) {
                System.out.println("\nProblem loading test.propeties.");
            }
        }
    }

    protected String getPropertyForTest(String key) {
        return configTest.getProperty(key);
    }

}
