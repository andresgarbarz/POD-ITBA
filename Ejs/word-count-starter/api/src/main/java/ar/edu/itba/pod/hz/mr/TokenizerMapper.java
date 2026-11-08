package ar.edu.itba.pod.hz.mr;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.regex.Pattern;

public class TokenizerMapper implements Mapper<String, String, String, Long> {

    private static final Long ONE = 1L;
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    @Override
    public void map(String key, String value, Context<String, Long> context) {
        WHITESPACE.splitAsStream(value.toLowerCase())
                .filter(word -> !word.isEmpty())
                .forEach(word -> context.emit(word, ONE));
    }
}
