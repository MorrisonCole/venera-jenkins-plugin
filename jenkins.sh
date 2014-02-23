#!/bin/bash

export MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n"
cd ~/code/ucl/heisentest/trunk
mvn hpi:run
