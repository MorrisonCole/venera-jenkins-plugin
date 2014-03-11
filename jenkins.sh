#!/bin/bash

# Set the working directory to the script location
cd ${0%/*}

# Run Jenkins
export MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n"
mvn hpi:run
