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


import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.report.pdf.PDFResources;
import org.sonar.report.pdf.entity.EntityUtils;
import org.sonar.report.pdf.entity.FileInfo;
import org.sonar.report.pdf.entity.FileInfoTypes;
import org.sonar.report.pdf.entity.Measures;
import org.sonar.report.pdf.entity.Project;
import org.sonar.report.pdf.entity.Qprofile;
import org.sonar.report.pdf.entity.Rule;
import org.sonar.report.pdf.entity.Severity;
import org.sonar.report.pdf.entity.Violation;
import org.sonar.report.pdf.entity.exception.ReportException;
import org.sonar.report.pdf.util.MetricKeys;
import org.sonarqube.ws.client.JdkUtils;
import org.sonarqube.ws.client.WSClient;
import org.sonarqube.ws.model.Components;
import org.sonarqube.ws.model.ComponentsTree;
import org.sonarqube.ws.model.Facet.ValueBean;
import org.sonarqube.ws.model.Issue;
import org.sonarqube.ws.model.Issues;
import org.sonarqube.ws.model.MeasuresComponent;
import org.sonarqube.ws.model.MeasuresComponentsTree;
import org.sonarqube.ws.query.ComponentsQuery;
import org.sonarqube.ws.query.IssueQuery;
import org.sonarqube.ws.query.MeasuresComponentTreeQuery;
import org.sonarqube.ws.query.RuleQuery;


/**
 * Builder for the whole project
 *
 */
public class ProjectBuilder extends AbstractBuilder {

    /**
     * 
     */
    private static final long serialVersionUID = -2813326260092889127L;

    private static final Logger LOG = LoggerFactory.getLogger("org.sonar.PDF");

    private static ProjectBuilder builder;

    private WSClient sonar;

    private Integer tableLimit;
    private static final String  S="metric";

    /**
     * Default constructor
     * 
     * @param sonar
     *            sonar
     */
    private ProjectBuilder(final WSClient sonar) {
        this.sonar = sonar;
        URL resourceText = this.getClass().getClassLoader().getResource(PDFResources.REPORT_PROPERTIES);
        Properties config = new Properties();
        try {
            config.load(resourceText.openStream());
        } catch (IOException e) {
            LOG.error("\nProblem loading report.properties.", e);
        }
        tableLimit = Integer.valueOf(config.getProperty(PDFResources.SONAR_TABLE_LIMIT));
    }

    public static ProjectBuilder getInstance(final WSClient sonar) {
        if (builder == null) {
            builder = new ProjectBuilder(sonar);
        }

        return builder;
    }

    /**
     * Initialize: <br>
     * - Project basic data <br>
     * - Project measures <br>
     * - Project categories violations <br>
     * - Project most violated rules<br>
     * - Project most violated files<br>
     * - Project most duplicated files<br>
     * 
     * @param projectKey
     *            projectKey
     * 
     * @throws ReportException
     *             ReportException
     */
    
    public Project initializeProject(final String projectKey) throws ReportException {
        Project project = new Project(projectKey);

        LOG.info("Retrieving project info for {} " ,project.getKey());
        ComponentsQuery rq = ComponentsQuery.create(project.getKey());
        rq.setstrategy("children");
        rq.setqualifiers("BRC");
        ComponentsTree resources = sonar.find(rq);

        if (resources != null) {
            initFromNode(project, resources);
            initMeasures(project);
            initResolutionsIssues(project);
            initMostViolatedRulesList(project);
            initMostViolatedFiles(project);
            initMostComplexElements(project);
            initMostDuplicatedFiles(project);
            initQprofilerules(project);
            
            
            LOG.debug("Accessing Sonar: getting child projects");

            List<Components> childNodes = resources.getComponents();

            Iterator<Components> it = childNodes.iterator();
            project.setSubprojects(new ArrayList<Project>(0));
            if (!it.hasNext()) {
                LOG.debug(project.getKey() + " project has no childs");
            }
            while (it.hasNext()) {
            	Components childNode = it.next();
                if (!projectKey.equals(childNode.getKey())) {
                    Project childProject = initializeProject(childNode.getKey());
                    project.getSubprojects().add(childProject);
                }
            }
        } else {
            LOG.info("Can't retrieve project info. Have you set username/password in Sonar settings?");
            throw new ReportException("Can't retrieve project info. Parent project node is empty. Authentication?");
        }

        return project;
    }

    /**
     * Initialize project object and his childs (except categories violations).
     * 
     * @param project
     *            project
     * @param resources
     *            resourceNode
     */
    
    private void initFromNode(Project project, final ComponentsTree resources) {
        project.setName(resources.getBaseComponent().getName());
        project.setDescription(resources.getBaseComponent().getDescription());
        project.setSubprojects(new LinkedList<Project>());
        project.setFixedissues(0);
        project.setRemovedissues(0);
        project.setMostViolatedRules(new LinkedList<Rule>());
        project.setMostComplexFiles(new LinkedList<FileInfo>());
        project.setMostDuplicatedFiles(new LinkedList<FileInfo>());
        project.setMostViolatedFiles(new LinkedList<FileInfo>());
        project.setQprofile(new LinkedList<Qprofile>());
    }
  
    /**
     * Initialize measures
     * 
     * @param project
     *            project
     * @throws ReportException
     *             ReportException
     */
    private void initMeasures(final Project project) throws ReportException {
        LOG.info("    Retrieving measures");
        MeasuresBuilder measuresBuilder = MeasuresBuilder.getInstance(sonar);
        Measures measures = measuresBuilder.initMeasuresByProjectKey(project.getKey());
        project.setMeasures(measures);
    }
  
    
    private void initResolutionsIssues(final Project project) throws ReportException {
        LOG.info("    Retrieving Resolutions Issues");	
		project.setFixedissues(getIssuesbyResolutions(project,"FIXED"));	
		project.setRemovedissues(getIssuesbyResolutions(project,"REMOVED"));
		
    }

	private int getIssuesbyResolutions(final Project project,String resolutions) throws ReportException {
		IssueQuery query = IssueQuery.create();
        query.componentKeys(project.getKey());
		query.resolved(true);
		query.resolutions(resolutions);
		Issues result = sonar.find(query);
		return result.getPaging().total();
	}
	   
	 private void initMostViolatedRulesList(final Project project) throws ReportException {
	        LOG.info("    Retrieving most violated rules");
	        List<Rule> allrules = new ArrayList<>();
	        IssueQuery query = IssueQuery.create();
			query.componentKeys(project.getKey());
			query.resolved(false);
			query.facets("rules");
			query.pageSize(1);
			Issues result = sonar.find(query);		
			List<ValueBean> issuescountByRules = result.getRuleFacet();
			if(issuescountByRules.isEmpty()) {
				project.setMostViolatedRuleslist(allrules); 
				return;
			}
		
			int size=((issuescountByRules.size()>10)?10:issuescountByRules.size());			
			for (Integer i = 0;i<size;i++) {
		            String ruleKey =issuescountByRules.get(i).getval();
		            RuleQuery rulequery = RuleQuery.create(ruleKey);
		            org.sonarqube.ws.model.Rules rules = sonar.find(rulequery);
		            if (rules == null || rules.getRules() == null || rules.getRules().size() != 1) {
		                LOG.error("There is no result on select rule from service");
		            } else {
		            	Rule rule = new Rule();
		            	rule.setKey(ruleKey);
		            	rule.setName(rules.getRules().get(0).getName());
		            	rule.setSeverity(Severity.get(rules.getRules().get(0).getSeverity()));
		            	rule.setViolationsNumber(issuescountByRules.get(i).getcount().toString());
		            	allrules.add(rule);
		           }		               		            
		       }
								
	
			project.setMostViolatedRuleslist(allrules); 
		
			LOG.info("    Retrieving all violated rules");
			int issuesize = 1;
			List<Issue> issuesByLevel = new ArrayList<>();
			Map<String, IssueBean> issues = new HashMap<>();
			ValueComparator bvc = new ValueComparator(issues);
			TreeMap<String, IssueBean> sortedMap = new TreeMap<>(bvc);
			for (int j = 0;j<issuescountByRules.size(); j++) {
				for (int i = 1;i<=issuesize; i++) {
					IssueQuery issuequery = IssueQuery.create();
					issuequery.componentKeys(project.getKey());
					issuequery.resolved(false);
					issuequery.rules(issuescountByRules.get(j).getval());
					issuequery.pageSize(500);
					issuequery.pageIndex(i);
					Issues issueresult = sonar.find(issuequery);
					int total=issueresult.getPaging().total();
					if ((int)Math.ceil((double)total/500)>20) {
						issuesize=20;
					}else {issuesize=(int)Math.ceil((double)total/500);
					}
					List<Issue>  tempissuesByLevel = issueresult.getIssues();
					issuesByLevel.addAll(tempissuesByLevel);
				}
				
			}
	         if (!issuesByLevel.isEmpty()) {
	            initMostViolatedRulesFromNode(issuesByLevel, issues);
	         } else {
	            LOG.debug("There is no result on select //resources/resource");
	            LOG.debug("There are no violations");
	         }

	        // sort the items of the map by list size
	        sortedMap.putAll(issues);
	        for (Entry<String, IssueBean> entry : sortedMap.entrySet()) {
	            String ruleKey = entry.getKey();
	            RuleQuery rquery = RuleQuery.create(ruleKey);
	            org.sonarqube.ws.model.Rules rules = sonar.find(rquery);
	            if (rules == null || rules.getRules() == null || rules.getRules().size() != 1) {
	                LOG.error("There is no result on select rule from service");
	            } else {	            	
	                project.getMostViolatedRules().add(defineRule(entry, rules));
	            }
	        }
			
	
	    }


    /**
     * Define Rule from sonar Rules
     * 
     * @param entry
     *            entry
     * @param rules
     *            rules
     * @return Rule
     */
    private Rule defineRule(Entry<String, IssueBean> entry, org.sonarqube.ws.model.Rules rules) {
        org.sonarqube.ws.model.Rule ruleNode = rules.getRules().get(0);
        Rule rule = new Rule();
        rule.setKey(ruleNode.getKey());
        rule.setName(ruleNode.getName());
        rule.setDescription(ruleNode.getDescription());
        rule.setSeverity(entry.getValue().getSeverity());
        rule.setViolationsNumber(Integer.toString(entry.getValue().getIssues().size()));
        // setTopViolations
        List<Violation> violations = new ArrayList<>();
        for (Issue issue : entry.getValue().getIssues()) {
            String line;
            if (issue.getLine() == null) {
                line = EntityUtils.NA_METRICS.getKey();
            } else {
                line = "" + issue.getLine();
            }
            Violation violation = new Violation(line, issue.getComponent());
            violations.add(violation);
        }
        rule.setTopViolations(violations);
        return rule;
    }

    /**
     * Initialize most violated files
     * 
     * @param project
     *            project
     * @throws ReportException
     *             ReportException
     */
    private void initMostViolatedFiles(final Project project) throws ReportException {
        LOG.info("    Retrieving most violated files");
        LOG.debug("Accessing Sonar: getting most violated files");

        MeasuresComponentTreeQuery resourceQuery = MeasuresComponentTreeQuery.createForMetrics(project.getKey(), MetricKeys.VIOLATIONS);
        resourceQuery.setQualifiers("FIL");
        resourceQuery.setMetricSort(MetricKeys.VIOLATIONS);
        resourceQuery.setS(S);        
        resourceQuery.setAsc(false);
        resourceQuery.setPs(tableLimit);
        MeasuresComponentsTree resources = sonar.find(resourceQuery);
        List<MeasuresComponent> filecomponents =resources.getComponents();
        List<FileInfo> fileInfoList = FileInfoBuilder.initFromDocument(filecomponents, FileInfoTypes.VIOLATIONS_CONTENT);
        project.setMostViolatedFiles(fileInfoList);

    }

    /**
     * Initialize most complex files
     * 
     * @param project
     *            project
     * @throws ReportException
     *             ReportException
     */
    private void initMostComplexElements(final Project project) throws ReportException {
        LOG.info("    Retrieving most complex elements");
        LOG.debug("Accessing Sonar: getting most complex elements");
        
        MeasuresComponentTreeQuery resourceQuery = MeasuresComponentTreeQuery.createForMetrics(project.getKey(), MetricKeys.COMPLEXITY);
        resourceQuery.setQualifiers("FIL");
        resourceQuery.setMetricSort(MetricKeys.COMPLEXITY);
        resourceQuery.setS(S);        
        resourceQuery.setAsc(false);
        resourceQuery.setPs(tableLimit);
        MeasuresComponentsTree resources = sonar.find(resourceQuery);
        List<MeasuresComponent> filecomponents =resources.getComponents();
        List<FileInfo> fileInfoList = FileInfoBuilder.initFromDocument(filecomponents, FileInfoTypes.CCN_CONTENT);
        project.setMostComplexFiles(fileInfoList);
    }

    /**
     * Initialize most duplicated files
     * 
     * @param project
     *            project
     * @throws ReportException
     *             ReportException
     */
    private void initMostDuplicatedFiles(final Project project) throws ReportException {
        LOG.info("    Retrieving most duplicated files");
        LOG.debug("Accessing Sonar: getting most duplicated files");
        MeasuresComponentTreeQuery resourceQuery = MeasuresComponentTreeQuery.createForMetrics(project.getKey(), MetricKeys.DUPLICATED_LINES);
        resourceQuery.setQualifiers("FIL");
        resourceQuery.setMetricSort(MetricKeys.DUPLICATED_LINES);
        resourceQuery.setS(S);        
        resourceQuery.setAsc(false);
        resourceQuery.setPs(tableLimit);
        MeasuresComponentsTree resources = sonar.find(resourceQuery);
        List<MeasuresComponent> filecomponents =resources.getComponents();
        List<FileInfo> fileInfoList = FileInfoBuilder.initFromDocument(filecomponents, FileInfoTypes.DUPLICATIONS_CONTENT);
        project.setMostDuplicatedFiles(fileInfoList);
    }

    /**
     * Initialize most violated rules
     * 
     * @param issuesByLevel
     *            issuesByLevel
     * @param issues
     *            issues map
     * @return number of fules added
     * @throws ReportException
     */
    private void initMostViolatedRulesFromNode(final List<Issue> issuesByLevel, Map<String, IssueBean> issues)
            throws ReportException {
        for (Issue issue : issuesByLevel) {
            String ruleKey = issue.getRule();
            if (issues.containsKey(ruleKey)) {
                // adds Issue to the List of current issues for the key
                IssueBean bean = issues.get(ruleKey);
                bean.getIssues().add(issue);
            } else {
                // adds Issue to a List for a new key
                List<Issue> issuesForKey = new ArrayList<>();
                IssueBean bean = new IssueBean();
                bean.setSeverity(Severity.get(issue.getSeverity()));
                issuesForKey.add(issue);
                bean.setIssues(issuesForKey);
                issues.put(ruleKey, bean);
            }
        }

    }
    
    /**
     * Initialize Qprofilerules
     * 
     * @param project
     *            current project
     */
    private void initQprofilerules(final Project project) {
    	 LOG.info("    Retrieving Qprofile rules");
    	 String qualityProfile = project.getMeasure(MetricKeys.PROFILE).getDataValue();
    	 List<Qprofile> qprofiles = new ArrayList<>();
         if (qualityProfile !=null && !qualityProfile.isEmpty()) {
         JSONParser parser = new JSONParser();
         JSONArray json;
         try {
			json = (JSONArray) parser.parse(qualityProfile);
			if (!json.isEmpty()) {
				for (int i=0; i<json.size();i++)
					{
						 Map<String, String> properties = JdkUtils.getInstance().getFieldsWithValues(json.get(i));
						 if (properties.containsKey("key")) {
							 Qprofile qprofile = new Qprofile();
							 qprofile.setKey(properties.get("key"));
							 qprofile.setName(properties.get("name"));
							 qprofile.setLanguage(properties.get("language"));
							 List<org.sonarqube.ws.model.Rule> rulesByQprofile = getQprofilerules(properties.get("key"));
							 qprofile.setQprofileRules(rulesByQprofile);
							 qprofiles.add(qprofile);
						 }
	             }
				project.setQprofile(qprofiles);
	        }
		} catch (ParseException | ReportException e) {
			 LOG.error("Can not get Qprofilerules. ", e);
		}
        
    }
    }

    /**
     * get Qprofilerules
     * 
     * @param qprofiles
     *            current qprofiles
     * @return List<org.sonarqube.ws.model.Rule>
     * @throws ReportException 
     */
	private List<org.sonarqube.ws.model.Rule> getQprofilerules(String qprofile) throws ReportException {
		List<org.sonarqube.ws.model.Rule> rulesByQprofile = new ArrayList<>();
		int size = 1;
		for (int i = 1;i<=size; i++) {
			RuleQuery query = RuleQuery.createqprofile();
			query.setActivation(true);
			query.setQprofile(qprofile);
			query.setP(i);
			query.setPs(100);
			query.setF("name,severity,langName");
			org.sonarqube.ws.model.Rules rules = sonar.find(query);
			int total=rules.getTotal();
			size=(int)Math.ceil((double)total/100);
			List<org.sonarqube.ws.model.Rule>  temprules = rules.getRules();
			rulesByQprofile.addAll(temprules);
		}			 
		return rulesByQprofile;
	}

    /**
     * Container of issues
     *
     */
    static class IssueBean implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -1162084100076730465L;
        private Severity severity;
        private List<Issue> issues;

        public Severity getSeverity() {
            return severity;
        }

        public void setSeverity(Severity severity) {
            this.severity = severity;
        }

        public List<Issue> getIssues() {
            return issues;
        }

        public void setIssues(List<Issue> issues) {
            this.issues = issues;
        }

        @Override
        public String toString() {
            return getSeverity() + " : size = " + issues.size();
        }

    }

    
    /**
     * Comparator to sort issues
     *
     */
    static class ValueComparator implements Comparator<String>, Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -8156307906104648499L;
        private  Map<String, IssueBean> base;

        public ValueComparator(Map<String, IssueBean> base) {
            this.base = base;
        }

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(String ruleKey1, String ruleKey2) {
            IssueBean beanA = base.get(ruleKey1);
            IssueBean beanB = base.get(ruleKey2);
           
            if (beanA.getIssues().size() > beanB.getIssues().size()) {
                return -1;
            } else {
                return 1;
            }
          
        }
    }

}
