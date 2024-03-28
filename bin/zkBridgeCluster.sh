#!/usr/bin/env bash
mkdir -p /tmp/zookeeper
echo 0 >> /tmp/zookeeper/myid
mkdir -p /tmp/zookeeper1
echo 1 >> /tmp/zookeeper/myid
mkdir -p /tmp/zookeeper2
echo 2 >> /tmp/zookeeper/myid

ZOO_LOG_DIR=./logs ./bin/zkServer.sh --config ./conf $1
ZOO_LOG_DIR=./logs1 ./bin/zkServer.sh --config ./conf1 $1
ZOO_LOG_DIR=./logs2 ./bin/zkServer.sh --config ./conf2 $1
