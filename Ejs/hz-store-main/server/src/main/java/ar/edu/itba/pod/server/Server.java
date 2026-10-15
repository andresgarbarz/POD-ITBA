package ar.edu.itba.pod.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    static void main() {
        logger.info("hz-store Server Starting ...");

        Config config = new Config();

        GroupConfig groupConfig = new GroupConfig()
                .setName("65488")
                .setPassword("65488");
        config.setGroupConfig(groupConfig);

        NetworkConfig networkConfig = new NetworkConfig();
        JoinConfig join = networkConfig.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);

        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(List.of("127.0.0.1"))
                .setEnabled(true);
        networkConfig.setInterfaces(interfacesConfig);
        config.setNetworkConfig(networkConfig);

        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        Map<String, String> datos = hz.getMap("materias");
        datos.put("72.42", "POD");
        IO.println("%d Datos en el cluster".formatted(datos.size()));
        for (String key : datos.keySet())  {
            IO.println("Datos con key %s= %s".formatted(key, datos.get(key)));
        }
    }

}
