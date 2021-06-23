#!/bin/bash

rm -rf bin #removes bin folder

mkdir bin #creates a new bin folder

javac -d bin -sourcepath src -cp lib/*:lib/javafx-sdk-11.0.2/lib/*:src $(find src -name *.java) --module-path lib/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls,javafx.media #compiles in folder bin from folder src (all files that end in .java) with Classpath set in lib and src(for classes)

java -cp lib/*:lib/javafx-sdk-11.0.2/lib/*:bin --module-path lib/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.media Main $@ #starts the app and passes all arguments

#read var #Keeps terminal open
