# Apache Beam Examples

## Why do this? What is the purpose of this repository?

Why? Because I want to learn about Apache Beam, and often the best way of learning is by doing.

The purpose:

> Create a store of Apache Beam Pipelines that can be referenced, copied or used at another point in time,
for different purpose but following the same or similar methodologies.

## Current Examples

- ManualOutputs - Apache Beam SDK for Java == 2.2.0. This pipeline takes a simple data set, and uses an array of strings to filter,
and produce a number of files, associated with the log level. Right now this pipeline is pretty inflexibile,
because what if we wanted to add, remove or change log levels?
To run this pipeline, provide the following arguments:
```s
$ mvn compile exec:java -Dexec.mainClass=io.dmh.apache.beam.examples.ManualOutputs \
  "-Dexec.args=--inputFile=data/in/log_file.txt --output=data/out/manual" \
  -Pdirect-runner
```
