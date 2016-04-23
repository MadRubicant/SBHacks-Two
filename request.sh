#!/bin/bash

mvn compile && echo "test-images/pill.jpg" | mvn exec:java -Dexec.mainClass="Main"
