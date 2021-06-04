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

import org.sonar.report.pdf.entity.Measure;
import org.sonarqube.ws.model.ComponentMeasure;

/**
 * Builder for a measure
 *
 */
public class MeasureBuilder extends AbstractBuilder {

    /**
     * 
     */
    private static final long serialVersionUID = -4923945826578916614L;

    private MeasureBuilder() {
        super();
    }

    /**
     * Init measure from XML node. The root node must be "msr".
     * 
     * @param componentMeasure
     * @return
     */
    public static Measure initFromNode(final ComponentMeasure componentMeasure) {
        Measure measure = new Measure();
        String metricvalue = componentMeasure.getMetricValue();
        String metricname = componentMeasure.getMetric();
            
        if (metricvalue != null && metricname !=null) {
        	 measure.setKey(metricname);
             measure.setValue(String.valueOf(metricvalue));
             measure.setDataValue(metricvalue);
        	if (metricname.equalsIgnoreCase("reliability_remediation_effort") || metricname.equalsIgnoreCase("security_remediation_effort") || metricname.equalsIgnoreCase("sqale_index")) {
        		measure.setFormatValue(effortTimeConvert(metricvalue));
        	}else {
        		 measure.setFormatValue(metricvalue);
        	}
        }

        return measure;
    }

	
	private static String effortTimeConvert(String metricvalue) {
		String result=metricvalue;
		int time= Integer.parseInt(metricvalue);
		int days=time / (60 * 24);
		time = time % (60 * 24);
		int hours = time /60 ;
		int minute = time % 60;
		
		if (days >0 && hours==0 && minute==0) {
			result = days +"d";
		}
		if (days >0 && (hours> 0 || minute >0)) {
			result = days +"d" + hours + "h" + minute + "min";
		}
		
		if (days==0 && hours > 0) {
			result = hours + "h" + minute + "min";
		}
		
		if (days==0 && hours == 0) {
			result = minute + "min";
		}
		return result;
	}
}
