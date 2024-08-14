#!/bin/bash

# JDK路径
JAVA_HOME=/usr/lib/jvm/java-17

# 项目根目录
PROJECT_HOME=$(dirname $(readlink -f $0))

# 动态获取JAR包名称
JAR_NAME=$(ls $PROJECT_HOME/*.jar | grep -v 'original' | head -n 1)

# 检查JAR包是否存在
if [ -z "$JAR_NAME" ]; then
  echo "Error: No JAR file found in $PROJECT_HOME"
  exit 1
fi

# Java可执行文件路径
JAVA_CMD="$JAVA_HOME/bin/java"

# 检查JAVA_HOME是否存在
if [ ! -d "$JAVA_HOME" ]; then
  echo "Error: JAVA_HOME directory does not exist: $JAVA_HOME"
  exit 1
fi

# 配置文件路径
CONFIG_LOCATION="$PROJECT_HOME/config/"

# 构建CLASSPATH
CLASSPATH="$PROJECT_HOME/config:$PROJECT_HOME/lib/*"

# 启动JAR包
echo "Starting application with $JAVA_CMD and JAR $JAR_NAME..."
$JAVA_CMD -cp "$CLASSPATH:$JAR_NAME" com.feilu.Application --spring.config.location=$CONFIG_LOCATION
