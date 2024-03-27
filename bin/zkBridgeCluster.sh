#!/usr/bin/env bash
ZOO_LOG_DIR=./logs ./bin/zkServer.sh --config ./conf $1
ZOO_LOG_DIR=./logs1 ./bin/zkServer.sh --config ./conf1 $1
ZOO_LOG_DIR=./logs2 ./bin/zkServer.sh --config ./conf2 $1
