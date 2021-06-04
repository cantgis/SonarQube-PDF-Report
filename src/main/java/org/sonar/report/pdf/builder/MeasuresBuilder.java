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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.report.pdf.entity.Measure;
import org.sonar.report.pdf.entity.Measures;
import org.sonar.report.pdf.entity.exception.ReportException;
import org.sonar.report.pdf.util.MetricKeys;
import org.sonarqube.ws.client.WSClient;
import org.sonarqube.ws.model.Analyses;
import org.sonarqube.ws.model.ComponentMeasure;
import org.sonarqube.ws.model.Events;
import org.sonarqube.ws.model.MeasuresComponent;
import org.sonarqube.ws.model.MeasuresComponents;
import org.sonarqube.ws.query.MeasuresComponentQuery;
import org.sonarqube.ws.query.ProjectAnalysesQuery;

/**
 * Builder for a set of measures
 *
 */
public class MeasuresBuilder extends AbstractBuilder {

    /**
     * 
     */
    private static final long serialVersionUID = 6613369345856603442L;

    private static final Logger LOG = LoggerFactory.getLogger(MeasuresBuilder.class);

    private static MeasuresBuilder builder;

    private WSClient sonar;

    private transient List<String> measuresKeys = null;

    private static final Integer DEFAULT_SPLIT_LIMIT = 20;

    public MeasuresBuilder(final WSClient sonar) {
        this.sonar = sonar;
    }

    public static MeasuresBuilder getInstance(final WSClient sonar) {
        if (builder == null) {
            builder = new MeasuresBuilder(sonar);
        }
        return builder;
    }

    /**
     * Get the metric keys from MetricKeys
     * 
     * @return List of Keys
     * @throws ReportException
     *             ReportException
     */
    
    public List<String> getAllMetricKeys() throws ReportException {
    	Field[] fields = MetricKeys.class.getFields();
		List<String> allMetricKeys = new ArrayList<>();
    	for (int i = 0; i < fields.length; i++) {
			String metricKey;
			try {
				metricKey = ((MetricKeys) fields[i].get(MetricKeys.class)).getKey();
		        allMetricKeys.add(metricKey);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				 LOG.error( "Problem getting the metric keys .", e);
				 throw new ReportException("Problem getting the metric keys .");
			} 
		}
    	 return allMetricKeys;

       

    }
    
  /**
    public List<String> getAllMetricKeys() throws ReportException {

        MetricQuery query = MetricQuery.all();
		query.pageSize(500);
        Metrics metrics = sonar.find(query);
        List<String> allMetricKeys = new ArrayList<>();
        Iterator<Metric> it = metrics.getMetrics().iterator();
        while (it.hasNext()) {
            allMetricKeys.add(it.next().getKey());
        }
        return allMetricKeys;
    }
**/
    /**
     * Initialization of measures of a project
     * 
     * @param projectKey
     *            key of the project
     * @return Measures
     * @throws ReportException
     *             ReportException
     */
    public Measures initMeasuresByProjectKey(final String projectKey) throws ReportException {

        Measures measures = new Measures();
        if (measuresKeys == null) {
            measuresKeys = getAllMetricKeys();
        }
        // Avoid "Post too large"
        if (measuresKeys.size() > DEFAULT_SPLIT_LIMIT) {
            initMeasuresSplittingRequests(measures, projectKey);
        } else {
            this.addMeasures(measures, measuresKeys, projectKey);
        }

        return measures;

    }

    /**
     * This method does the required requests to get all measures from Sonar,
     * but taking care to avoid too large requests (measures are taken by 20).
     * 
     * @param measures
     *            measures
     * @param projectKey
     *            projectKey
     * @throws ReportException
     *             ReportException
     */
    private void initMeasuresSplittingRequests(final Measures measures, final String projectKey)
            throws ReportException {
        Iterator<String> it = measuresKeys.iterator();
        List<String> twentyMeasures = new ArrayList<>(20);
        int i = 0;
        while (it.hasNext()) {
            twentyMeasures.add(it.next());
            i++;
            if (i % DEFAULT_SPLIT_LIMIT == 0) {
                addMeasures(measures, twentyMeasures, projectKey);
                i = 0;
                twentyMeasures.clear();
            }
        }
        if (i != 0) {
            addMeasures(measures, twentyMeasures, projectKey);
        }
    }

    /**
     * Add measures to this.
     *
     * @param measures
     *            measures
     * @param measuresAsString
     *            measuresAsString
     * @param projectKey
     *            projectKey
     * @throws ReportException
     *             ReportException
     */
    private void addMeasures(final Measures measures, final List<String> measuresAsString, final String projectKey)
            throws ReportException {

        String[] measuresAsArray = measuresAsString.toArray(new String[measuresAsString.size()]);
        MeasuresComponentQuery resourceQuery = MeasuresComponentQuery.createForMetrics(projectKey, measuresAsArray);
        MeasuresComponents resources = sonar.find(resourceQuery);
        if (resources != null ) {
            this.addAllMeasuresFromDocument(projectKey, measures, resources.getMeasuresComponent());
        } else {
        	String e = measuresAsString.toString();
            LOG.debug("Wrong response when looking for measures: {}", e);
        }
    }

    /**
     * Add all measures from a document
     * 
     * @param projectKey
     *            projectKey
     * @param measures
     *            measures
     * @param measuresComponent
     *            resource
     * @throws ReportException
     *             ReportException
     */
    
    private void addAllMeasuresFromDocument(final String projectKey, final Measures measures, final MeasuresComponent measuresComponent)
            throws ReportException {
        List<ComponentMeasure> allNodes = measuresComponent.getMeasures();
        Iterator<ComponentMeasure> it = allNodes.iterator();
        String versionNode="1.0.0";
        while (it.hasNext()) {
            addMeasureFromNode(measures, it.next());
        }
      	try {       
            	ProjectAnalysesQuery aq = ProjectAnalysesQuery.create(projectKey);
            	aq.setcategory("VERSION");
            	aq.setps(1);
            	Analyses analyses = sonar.find(aq);       	
                Date dateNode = analyses.getAnalyses().get(0).getDate();
                if (dateNode != null) {
                    measures.setDate(dateNode);
                }
                
                for(Events events:analyses.getAnalyses().get(0).getevents()) {
                	if(events.getCategory().equalsIgnoreCase("VERSION")) {
                         versionNode = events.getName();
                	}                	
                }
                
                measures.setVersion(versionNode);
        
      	}catch (Exception e) {
        	Date dt = new Date();  
        	measures.setDate(dt);
        }      	
}
    
    
    /**
     * Add a measure from a node
     * 
     * @param projectKey
     *            projectKey
     * @param measures
     *            measures
     * @param componentMeasure
     *            measureNode
     * @throws ReportException
     *             ReportException
     */
    
    private void addMeasureFromNode(final Measures measures,
            final ComponentMeasure componentMeasure) {
        Measure measure = MeasureBuilder.initFromNode(componentMeasure);
        measures.addMeasure(measure.getKey(), measure);
    }

}
