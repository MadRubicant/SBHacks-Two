#!/bin/bash

mvn compile && echo "test-images/$1" | mvn exec:java -Dexec.mainClass="Main"
