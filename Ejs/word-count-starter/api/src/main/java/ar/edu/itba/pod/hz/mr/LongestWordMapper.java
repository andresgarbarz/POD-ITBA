package ar.edu.itba.pod.hz.mr;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.regex.Pattern;

public class LongestWordMapper implements Mapper<String, String, String, String> {

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    @Override
    public void map(String key, String value, Context<String, String> context) {
        WHITESPACE.splitAsStream(value.toLowerCase())
                .filter(word -> word.matches("[a-z]+"))
                .forEach(word -> context.emit(word.substring(0, 1), word));
    }
}
