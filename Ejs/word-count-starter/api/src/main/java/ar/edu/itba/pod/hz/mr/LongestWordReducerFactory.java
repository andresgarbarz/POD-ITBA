package ar.edu.itba.pod.hz.mr;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class LongestWordReducerFactory implements ReducerFactory<String, String, String> {

    @Override
    public Reducer<String, String> newReducer(String key) {
        return new LongestWordReducer();
    }

    private static class LongestWordReducer extends Reducer<String, String> {
        private volatile String longest;

        @Override
        public void reduce(String word) {
            if (longest == null
                    || word.length() > longest.length()
                    || (word.length() == longest.length() && word.compareTo(longest) < 0)) {
                longest = word;
            }
        }

        @Override
        public String finalizeReduce() {
            return longest;
        }
    }
}
