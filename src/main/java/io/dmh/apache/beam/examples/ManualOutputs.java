
package io.dmh.apache.beam.examples;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

public class ManualOutputs {

    /**
     * This DoFunction, accepts an argument in its constructor which allows us
     * to parameterize the filter value.
     */
    static class FilterLinesFn extends DoFn<String, String> {
        final String arg;

        FilterLinesFn(String arg) {
            this.arg = arg;
        }

        @ProcessElement
        public void processElement(ProcessContext c) {
            if (c.element().startsWith(this.arg)) {
                c.output(c.element().toString());
            }
        }

    }

    interface CustomPipelineOptions extends PipelineOptions {

        /**
         * Set this option to specify the input file.
         */
        @Description("Path of the file to read from")
        String getInputFile();
        void setInputFile(String value);

        /**
         * Set this option to specify where to write the output.
         */
        @Description("Path of the file to write to")
        String getOutput();
        void setOutput(String value);
    }

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
        PCollection<String> lines = pipeline.apply("ReadLines",
                TextIO.read().from(options.getInputFile()));

        final String[] logLevels = new String[] { "DEBUG", "INFO", "WARNING",
                "ERROR", "CRITICAL" };
        for (String logLevel : logLevels) {
            PCollection<String> collection = lines.apply(
                    String.format("Filter%sLines", logLevel), ParDo.of(
                            new FilterLinesFn(logLevel)));

            collection.apply(String.format(String.format("Write%s", logLevel)),
                    TextIO.write().to(options.getOutput())
                            .withSuffix(String.format("-%s.txt", logLevel))
                            .withNumShards(1));
        }

        pipeline.run().waitUntilFinish();
    }
}
