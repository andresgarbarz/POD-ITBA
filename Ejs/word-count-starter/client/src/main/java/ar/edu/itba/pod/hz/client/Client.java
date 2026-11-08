package ar.edu.itba.pod.hz.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import ar.edu.itba.pod.hz.mr.First100LinesKeyPredicate;
import ar.edu.itba.pod.hz.mr.LongestWordMapper;
import ar.edu.itba.pod.hz.mr.LongestWordReducerFactory;
import ar.edu.itba.pod.hz.mr.TokenizerMapper;
import ar.edu.itba.pod.hz.mr.WordCountCollator;
import ar.edu.itba.pod.hz.mr.WordCountCombinerFactory;
import ar.edu.itba.pod.hz.mr.WordCountReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Client {

	private static final Logger logger = LoggerFactory.getLogger(Client.class);

	static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
		logger.info("hz-word-count Client Starting ...");

		try {
			// Group Config
			GroupConfig groupConfig = new GroupConfig().setName("65488").setPassword("65488-pass");

			// Client Network Config
			ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
			clientNetworkConfig.addAddress("127.0.0.1");

			// Client Config
			ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig).setNetworkConfig(clientNetworkConfig);

			// Node Client
			HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

			// Key Value Source
			IMap<String, String> wordsIMap = hazelcastInstance.getMap("words");
			KeyValueSource<String, String> wordsKeyValueSource = KeyValueSource.fromMap(wordsIMap);

			// Job Tracker
			JobTracker jobTracker = hazelcastInstance.getJobTracker("word-count");

			// Text File Reading and Key Value Source Loading
			final AtomicInteger auxKey = new AtomicInteger();
			try (Stream<String> lines = Files.lines(Paths.get(args[0]), StandardCharsets.UTF_8)) {
				lines.forEach(line -> wordsIMap.put(String.valueOf(auxKey.getAndIncrement()), line));
			}

			//
			// Clase 11: Ejercicio 1
			//

			// MapReduce Job
			Job<String, String> job = jobTracker.newJob(wordsKeyValueSource);
			ICompletableFuture<List<Map.Entry<String, Long>>> future = job
					.keyPredicate(new First100LinesKeyPredicate())
					.mapper(new TokenizerMapper())
					.combiner(new WordCountCombinerFactory())
					.reducer(new WordCountReducerFactory())
					.submit(new WordCountCollator());

			// Wait and retrieve the result
			List<Map.Entry<String, Long>> result = future.get();
			result.forEach(IO::println);

			//
			// Clase 11: Ejercicio 2
			//

			Job<String, String> longestJob = jobTracker.newJob(wordsKeyValueSource);
			ICompletableFuture<Map<String, String>> longestFuture = longestJob
					.mapper(new LongestWordMapper())
					.reducer(new LongestWordReducerFactory())
					.submit();

			Map<String, String> longestWords = longestFuture.get();
			longestWords.entrySet().stream()
					.sorted(Map.Entry.comparingByKey())
					.forEach(IO::println);
		} finally {
			HazelcastClient.shutdownAll();
		}
	}

}