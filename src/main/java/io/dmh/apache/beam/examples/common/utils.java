package io.dmh.apache.beam.examples.common;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

public class utils {

    public static class ConvertKVToStringFn extends SimpleFunction<KV<String, Long>, String> {
        @Override
        public String apply(KV<String, Long> element) {
            return String.format("%s: %d", element.getKey(), element.getValue());
        }
    }

    /**
     * This DoFunction, accepts an argument in its constructor which allows us
     * to parameterize the filter value.
     */
    public static class ExtractLinesDoFn extends DoFn<String, KV<String, String>> {

        @ProcessElement
        public void processElement(ProcessContext ctx) {
            String[] elements = ctx.element().split(" - ");
            KV<String, String> kv = KV.of(elements[0], elements[1]);
            ctx.output(kv);
        }

    }

    /**
     * Custom pipeline options
     */
    public interface CustomPipelineOptions extends PipelineOptions {

        /**
         * Set this option to specify the input file.
         */
        @Description("Path of the file to read from")
        @Validation.Required
        String getInputFile();
        void setInputFile(String value);

        /**
         * Set this option to specify where to write the output.
         */
        @Description("Path of the file to write to")
        @Validation.Required
        String getOutput();
        void setOutput(String value);
    }


}
