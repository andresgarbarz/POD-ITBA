package ar.edu.itba.pod.grpc.client;

import ar.edu.itba.pod.grpc.GreeterGrpc;
import ar.edu.itba.pod.grpc.HelloReply;
import ar.edu.itba.pod.grpc.HelloRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.client.GrpcChannelFactory;

@SpringBootApplication
public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    static void main(String[] args) {
        SpringApplication.run(Client.class, args);
    }

    @Bean
    GreeterGrpc.GreeterBlockingStub greeterStub(GrpcChannelFactory channels) {
        // return GreeterGrpc.newBlockingStub(channels.createChannel("cloud"));
		return GreeterGrpc.newBlockingStub(channels.createChannel("local"));
    }

    @Bean
    CommandLineRunner run(GreeterGrpc.GreeterBlockingStub stub) {
        return _ -> {
            HelloRequest request = HelloRequest.newBuilder().setName("Andy").build();
            HelloReply response = stub.sayHello(request);
            log.info(response.getMessage());
        };
    }

}
