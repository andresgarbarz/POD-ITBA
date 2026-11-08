package ar.edu.itba.pod.hz.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    static void main() {
        logger.info("hz-word-count Server Starting ...");

        // Config
        Config config = new Config();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("65488").setPassword("65488-pass");
        config.setGroupConfig(groupConfig);

        // Network Config
        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);

        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(Collections.singletonList("127.0.0.*")).setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig().setInterfaces(interfacesConfig).setJoin(joinConfig);

        config.setNetworkConfig(networkConfig);

        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
    }

}
