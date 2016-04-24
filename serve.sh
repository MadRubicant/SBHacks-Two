#!/bin/bash

mvn compile && echo "serve" | mvn exec:java -Dexec.mainClass="Main"
