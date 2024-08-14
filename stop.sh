#!/bin/bash

# 项目根目录
PROJECT_HOME=$(dirname $(readlink -f $0))

# 动态获取JAR包名称
JAR_NAME=$(ls $PROJECT_HOME/*.jar | grep -v 'original' | head -n 1)

# 获取运行该JAR包的进程ID
PID=$(ps -ef | grep $JAR_NAME | grep -v grep | awk '{print $2}')

# 检查进程ID是否存在
if [ -z "$PID" ]; then
  echo "No running process found for $JAR_NAME."
  exit 1
else
  echo "Stopping process $PID..."
  kill $PID
  echo "Process $PID stopped."
fi
