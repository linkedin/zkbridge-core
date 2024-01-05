#!/bin/sh

#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

echo There are $# arguments to $0: $*
if [[ $# -eq 0 ]] ; then
    echo 'Error: Invalid number of parameters provided.'
    echo 'Please provide arg1 (req): branch_name, version_override (opt): new_version'
    exit 0
fi

branch_name=$1
version=`grep -A 4 "<groupId>com.linkedin.zkbridge-server</groupId>" pom.xml | grep "<version>" | awk 'BEGIN {FS="[<,>]"};{print $3}'`

if [ "$#" -eq 2 ]; then
  new_version=$2
else
  timestamp=`date -u +'%Y%m%d%H%M'`
  version=`grep -A 4 "<groupId>com.linkedin.zkbridge-server</groupId>" pom.xml | grep "<version>" | awk 'BEGIN {FS="[<,>]"};{print $3}'`
  new_version=`echo $version | cut -d'-' -f1`-$branch_name-$timestamp

# Below version upgrade logic is left here just in case
#  minor_version=`echo $version | cut -d'.' -f3`
#  major_version=`echo $version | cut -d'.' -f1` # should be 0
#  submajor_version=`echo $version | cut -d'.' -f2`

#  new_minor_version=`expr $minor_version + 1`
#  new_version=`echo $version | sed -e "s/${minor_version}/${new_minor_version}/g"`
#  new_version="$major_version.$submajor_version.$new_minor_version"
fi
echo "bump up version: $version -> $new_version"

for MODULE in $(find . -name 'pom.xml')
do
  echo "bump up: $MODULE"
  sed -i "s/${version}/${new_version}/g" $MODULE
  grep -C 1 "$new_version" $MODULE
done
