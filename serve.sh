#!/bin/bash

while true; do
    mvn compile && echo "serve" | mvn exec:java -Dexec.mainClass="Main"
done
