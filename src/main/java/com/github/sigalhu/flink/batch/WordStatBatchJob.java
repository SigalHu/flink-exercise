package com.github.sigalhu.flink.batch;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.apache.flink.util.StringUtils;

/**
 * 批处理统计词频
 * 
 * @author huxujun
 * @date 2019/11/8
 */
public class WordStatBatchJob {

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> text = env.readTextFile(ClassLoader.getSystemResource("words.txt").getPath());
        text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
                for (String word : line.toLowerCase().split(" ")) {
                    if (!StringUtils.isNullOrWhitespaceOnly(word)) {
                        collector.collect(Tuple2.of(word, 1));
                    }
                }
            }
        }).groupBy(0).sum(1).print();
    }
}
