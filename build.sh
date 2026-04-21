#!/bin/bash
set -e
cd "$(dirname "$0")/src"
echo "Compiling..."
javac *.java
echo "Packaging JAR..."
jar cfm Chess.jar manifest.mf *.class images/
echo "Done. Run with: java -jar src/Chess.jar"
