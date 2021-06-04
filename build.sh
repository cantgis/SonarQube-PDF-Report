#!/bin/bash

# auto maven build
which "mvn" > /dev/null
if [ $? -eq 0 ]
then
    mvn -version
else
    echo "mvn not exist"
    exit 1
fi
# 使用mvn命令自动添加license
mvn license:format
# 构建项目
mvn clean package -Dmaven.test.skip=true

