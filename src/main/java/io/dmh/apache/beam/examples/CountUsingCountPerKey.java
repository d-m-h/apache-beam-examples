
package io.dmh.apache.beam.examples;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import io.dmh.apache.beam.examples.common.utils.CustomPipelineOptions;
import io.dmh.apache.beam.examples.common.utils.ConvertKVToStringFn;
import io.dmh.apache.beam.examples.common.utils.ExtractLinesDoFn;

public class CountUsingCountPerKey {

    /**
     * The main pipeline method. This is where the pipeline workflow execution
     * logic resides.
     */
    public static void main(String[] args) {
        // Parse the command line arguments.
        CustomPipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
                .as(CustomPipelineOptions.class);

        // Create the pipeline
        Pipeline pipeline = Pipeline.create(options);

        // Read the data in
        PCollection<String> lines = pipeline.apply("ReadLines", TextIO.read().from(options.getInputFile()));

        // Extract the key-value pairs by splitting the lines, where the Key is the log level
        PCollection<KV<String, String>> coll = lines.apply("ExtractLines", ParDo.of(new ExtractLinesDoFn()));

        // Count the data per key.
        PCollection<KV<String, Long>> counts = coll.apply("CountMessagesPerKey", Count.perKey());

        // Format the data for output
        PCollection<String> formatted = counts.apply("FormatCountsForOutput", MapElements.via(new ConvertKVToStringFn()));

        // Write that data out.
        formatted.apply("WriteOutCounts", TextIO.write().to(options.getOutput()).withSuffix(".txt").withNumShards(1));

        pipeline.run().waitUntilFinish();
    }
}
