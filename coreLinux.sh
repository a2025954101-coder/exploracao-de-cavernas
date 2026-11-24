#!/bin/bash
# Arquivo de comandos para compilação e execução do projeto
# LINUX - Sem Maven
set -e

echo "Compilando..."
mkdir -p bin
javac -cp "core.jar" -d bin/ src/*.java

echo "Executando..."
java -cp "bin:core.jar:." -ea Sketch
