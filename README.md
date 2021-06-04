# SonarQube-PDF-Report

Sonar PDF Report Plugin
=========================

适用SonarQube版本 : 8.8.0

编译:mvn clean package -Dmaven.test.skip=true

## Description / Features

本插件生成PDF格式的sonarqube报告.

报告包括以下内容:

* 概要

* 静态分析

* 动态分析

* 编码问题

* 热点:
  * 违反最多的规则TOP10
  * 违规最多的文件TOP5
  * 复杂度最高的文件TOP5
  * 重复行最多的文件TOP5

* 违规详情

* 子模块信息（只有在存在时生成）


## Installation

1. 下载对应的版本，将插件复制到SONARQUBE_HOME/extensions/plugins 目录
```
[Install the plugin through the [Update Center](http://docs.sonarqube.org/display/SONAR/Update+Center) or download it into the SONARQUBE_HOME/extensions/plugins directory]
```
1. 重启SonarQube
```
[Restart SonarQube]
```

## Usage

SonarQube PDF是一个 post-job任务. 因此，PDF 报告将在SonarQube分析结束后生成.
```
SonarQube PDF works as a post-job task. In this way, a PDF report is generated after each analysis in SonarQube.
```
### Configuration

配置Skip为“否”时生成PDF报告功能启用。

配置Username和Password。如果出现HTTP error: 401或者HTTP error: 403, msg: Forbidden，请查看该配置。

![Plugin Configuration](configuration.jpg?raw=true "Plugin Configuration")

### Download the report

PDF report can be downloaded from the SonarQube GUI: