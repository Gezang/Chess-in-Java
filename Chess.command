#!/bin/bash
cd "$(dirname "$0")/src"
if [ ! -f Chess.jar ]; then
    echo "Chess.jar not found. Building..."
    javac *.java
    jar cfm Chess.jar manifest.mf *.class images/
fi
java -jar Chess.jar
