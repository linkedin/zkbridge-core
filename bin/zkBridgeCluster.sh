#!/usr/bin/env bash
./bin/zkServer.sh --config ./conf $1
./bin/zkServer.sh --config ./conf1 $1
./bin/zkServer.sh --config ./conf2 $1
