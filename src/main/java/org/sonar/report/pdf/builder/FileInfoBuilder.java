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
package org.sonar.report.pdf.builder;

import java.util.LinkedList;
import java.util.List;

import org.sonar.report.pdf.entity.FileInfo;
import org.sonar.report.pdf.entity.FileInfoTypes;
import org.sonarqube.ws.model.MeasuresComponent;

/**
 * Builder for file information
 *
 */
public class FileInfoBuilder extends AbstractBuilder {

    /**
     * 
     */
    private static final long serialVersionUID = -1775338259201548335L;

    private FileInfoBuilder() {
        super();
    }

    /**
     * Initialization of File Information from resources
     * 
     * @param filecomponents
     *            resources
     * @param type
     *            FileInfoTypes
     * @return
     */
    public static List<FileInfo> initFromDocument(final List<MeasuresComponent> filecomponents, final FileInfoTypes type) {
        List<FileInfo> fileInfoList = new LinkedList<>();
        if (filecomponents != null) {
            for (MeasuresComponent fileNode : filecomponents) {
                FileInfo fileInfo = new FileInfo();
                initFromNode(fileInfo, fileNode, type);
                if (fileInfo.isContentSet(type)) {
                    fileInfoList.add(fileInfo);
                }

            }
        }
        return fileInfoList;
    }
    

    /**
     * A FileInfo object could contain information about violations, ccn or
     * duplications, this cases are distinguished in function of content param,
     * and defined by project context.
     * 
     * @param fileNode
     *            DOM Node that contains file info
     * @param type
     *            Type of content
     */
    public static void initFromNode(final FileInfo fileInfo, final MeasuresComponent fileNode, final FileInfoTypes type) {
        fileInfo.setKey(fileNode.getKey());
        fileInfo.setName(fileNode.getName());
        if (fileNode.getMeasures().size()>0){
        	if (type == FileInfoTypes.VIOLATIONS_CONTENT) {
        		fileInfo.setViolations(fileNode.getMeasures().get(0).getMetricValue());
        	} else if (type == FileInfoTypes.CCN_CONTENT) {
        		fileInfo.setComplexity(fileNode.getMeasures().get(0).getMetricValue());
        	} else if (type == FileInfoTypes.DUPLICATIONS_CONTENT) {
        		fileInfo.setDuplicatedLines(fileNode.getMeasures().get(0).getMetricValue());
        	}
        }else {
        	 fileInfo.setViolations("N/A");
        	 fileInfo.setComplexity("N/A");
        	 fileInfo.setDuplicatedLines("N/A");
        }
    }

}
