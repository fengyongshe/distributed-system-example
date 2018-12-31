package com.fys.flink.example.dataset;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.hadoop.mapreduce.HadoopInputFormat;
import org.apache.flink.api.java.hadoop.mapreduce.HadoopOutputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class HadoopWordCount {

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: WordCount <input path> <result path>");
      return;
    }

    final String inputPath = args[0];
    final String outputPath = args[1];

    final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
    Job job = Job.getInstance();
    HadoopInputFormat<LongWritable,Text> hadoopIF =
        new HadoopInputFormat<LongWritable, Text>(new TextInputFormat(), LongWritable.class, Text.class, job);
    TextInputFormat.addInputPath(job, new Path(inputPath));

    TypeInformation<Tuple2<LongWritable, Text>> info = TypeInformation.of(new TypeHint<Tuple2<LongWritable, Text>>(){});
    DataSet<Tuple2<LongWritable, Text>> text = env.createInput(hadoopIF, info);

    DataSet<Tuple2<String, Integer>> words = text.flatMap(new Tokenizer());
    DataSet<Tuple2<String, Integer>> result = words.groupBy(0).aggregate(Aggregations.SUM, 1);
    DataSet<Tuple2<Text, IntWritable>> hadoopResult = result.map(new HadoopDatatypeMapper());

    HadoopOutputFormat<Text, IntWritable> hadoopOutputFormat = new HadoopOutputFormat<Text, IntWritable>(new TextOutputFormat<Text, IntWritable>(), job);
    hadoopOutputFormat.getConfiguration().set("mapreduce.output.textoutputformat.separator", " ");
    hadoopOutputFormat.getConfiguration().set("mapred.textoutputformat.separator", " "); // set the value for both, since this test
    TextOutputFormat.setOutputPath(job, new Path(outputPath));

    // Output & Execute
    hadoopResult.output(hadoopOutputFormat);
    env.execute("Word Count");

  }

  public static final class Tokenizer extends RichFlatMapFunction<Tuple2<LongWritable, Text>, Tuple2<String, Integer>> {

    @Override
    public void flatMap(Tuple2<LongWritable, Text> value, Collector<Tuple2<String, Integer>> out) {
      // normalize and split the line
      String line = value.f1.toString();
      String[] tokens = line.toLowerCase().split("\\W+");

      // emit the pairs
      for (String token : tokens) {
        if (token.length() > 0) {
          out.collect(new Tuple2<String, Integer>(token, 1));
        }
      }
    }
  }

  public static final class HadoopDatatypeMapper extends RichMapFunction<Tuple2<String, Integer>, Tuple2<Text, IntWritable>> {

    @Override
    public Tuple2<Text, IntWritable> map(Tuple2<String, Integer> value) throws Exception {
      return new Tuple2<Text, IntWritable>(new Text(value.f0), new IntWritable(value.f1));
    }

  }
}
