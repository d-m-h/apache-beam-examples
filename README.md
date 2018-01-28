# Apache Beam Examples

## Why do this? What is the purpose of this repository?

Why? Because I want to learn about Apache Beam, and often the best way of learning is by doing.

The purpose:

> Create a store of Apache Beam Pipelines that can be referenced, copied or used at another point in time, 
either for the purpose of using it as a refresher, or making use of the code in other pipelines.

## Current Examples

- ManualOutputs: This pipeline takes a simple data set, and uses an array of strings to filter,
and produce a number of files, associated with the log level. Right now this pipeline is pretty inflexibile,
because what if we wanted to add, remove or change log levels?
To run this pipeline, provide the following arguments:
```shell
$ mvn compile exec:java -Dexec.mainClass=io.dmh.apache.beam.examples.ManualOutputs \
  "-Dexec.args=--inputFile=data/in/log_file.txt --output=data/out/manual" \
  -Pdirect-runner
```

- CountUsingIterable: This pipeline takes a simple data set, creates a set of Key-Value pairs, were the key is the
log level and the value is the log message. It then performs an explicit `GroupByKey()` where an `Iterable` is returned.
The length of the iterable is obtained, and is equivalent to the count.
```shell
$ mvn compile exec:java -Dexec.mainClass=io.dmh.apache.beam.examples.CountUsingIterable \
  "-Dexec.args=--inputFile=data/in/log_file.txt --output=data/out/count_by_iterable" \
  -Pdirect-runner
```

- CountUsingCountPerKey: The same as `CountUsingIterable`, but with one difference - the `Count.perKey()` functionality is used,
saving us a step.
```shell
$ mvn compile exec:java -Dexec.mainClass=io.dmh.apache.beam.examples.CountUsingCountPerKey \
  "-Dexec.args=--inputFile=data/in/log_file.txt --output=data/out/count_by_key" \
  -Pdirect-runner
```
