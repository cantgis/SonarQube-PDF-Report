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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.DateUtils;
import org.sonar.report.pdf.entity.exception.ReportException;
import org.sonarqube.ws.client.WSClient;
import org.sonarqube.ws.client.services.AbstractQuery;
import org.sonarqube.ws.model.MeasureHisto;
import org.sonarqube.ws.model.TimeMachines;
import org.sonarqube.ws.query.TimeMachineQuery;

/**
 * Builder for History information
 *
 */
public class HistoryBuilder extends AbstractBuilder {
    /**
     * 
     */
    private static final long serialVersionUID = 5905654675603249537L;

    private static final Logger LOG = LoggerFactory.getLogger(HistoryBuilder.class);
    private String projectKey;
    private static HistoryBuilder builder;

    private WSClient sonar;

    private HistoryBuilder(WSClient sonar, String projectKey) {
        super();
        this.projectKey = projectKey;
        this.sonar = sonar;
    }

    public static HistoryBuilder getInstance(final WSClient sonar, final String projectKey) {
        if (builder == null) {
            builder = new HistoryBuilder(sonar, projectKey);
        }
        return builder;
    }

    /**
     * Compute measure trends
     * 
     * @param measureNode
     *            measureNode
     * @return Integer
     * @throws ReportException
     *             ReportException
     */
    public Integer computeTrend(org.sonarqube.ws.model.Measure measureNode) throws ReportException {
        Integer trend = 0;
        Double currentValue = measureNode.getValue();
        TimeMachineQuery query = TimeMachineQuery.create();
        query.metrics(measureNode.getKey());
        query.format(AbstractQuery.JSON_FORMAT);
        query.resource(projectKey);
        query.fromDateTime(DateUtils.addDays(new Date(), -30));
        List<TimeMachines> histos = sonar.findAll(query);
        if (histos != null && !histos.isEmpty()) {
            List<MeasureHisto> measures = histos.get(0).getCells();
            Collections.sort(measures);
            Double oldValue = 0.0;
            if (!measures.isEmpty()) {
                String strVal = measures.get(0).getValue();
                if (strVal != null) {
                    try {

                        oldValue = Double.valueOf(strVal);
                    } catch (NumberFormatException e) {
                        LOG.debug("Error formatting value " + strVal + " for key" + measureNode.getKey(), e);
                    }
                }
            }
            if (oldValue != null && currentValue != null) {
                trend = currentValue.compareTo(oldValue);
            }
        }
        return trend;
    }

}
