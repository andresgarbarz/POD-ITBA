package ar.edu.itba.pod.grpc.client;

import ar.edu.itba.pod.grpc.user.LoginInformation;
import ar.edu.itba.pod.grpc.user.User;
import ar.edu.itba.pod.grpc.user.UserRoles;
import ar.edu.itba.pod.grpc.user.UserServiceGrpc;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.client.GrpcChannelFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    static void main(String[] args) {
        SpringApplication.run(Client.class, args);
    }

    @Bean
    UserServiceGrpc.UserServiceBlockingStub userStub(GrpcChannelFactory channels) {
        return UserServiceGrpc.newBlockingStub(channels.createChannel("local"));
    }

    @Bean
    UserServiceGrpc.UserServiceFutureStub userFutureStub(GrpcChannelFactory channels) {
        return UserServiceGrpc.newFutureStub(channels.createChannel("local"));
    }

    @Bean
    UserServiceGrpc.UserServiceStub userAsyncStub(GrpcChannelFactory channels) {
        return UserServiceGrpc.newStub(channels.createChannel("local"));
    }

    @Bean
    CommandLineRunner run(UserServiceGrpc.UserServiceBlockingStub stub,
            UserServiceGrpc.UserServiceFutureStub futureStub,
            UserServiceGrpc.UserServiceStub asyncStub) {
        return _ -> {
            LoginInformation login = LoginInformation.newBuilder()
                    .setUserName("foo")
                    .setPassword("foopass")
                    .build();
            User user = stub.doLogin(login);
            log.info("Logged in user: {}", user);

            getRolesWithCallback(futureStub, user);
            getRolesWithObserver(asyncStub, user);

            try {
                stub.doLogin(LoginInformation.newBuilder()
                        .setUserName("foo")
                        .setPassword("wrong")
                        .build());
            } catch (StatusRuntimeException ex) {
                log.error("Login with wrong password failed as expected: {}", ex.getStatus());
            }
        };
    }

    /**
     * Pide los roles de forma no bloqueante con FutureStub + callback.
     * El latch evita que el cliente termine antes de que llegue la respuesta.
     */
    static void getRolesWithCallback(UserServiceGrpc.UserServiceFutureStub futureStub, User user)
            throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            ListenableFuture<UserRoles> rolesFuture = futureStub.getRoles(user);
            Futures.addCallback(rolesFuture, new UserRolesCallback(latch), executor);
            if (!latch.await(5, TimeUnit.SECONDS)) {
                log.error("Timed out waiting for roles");
            }
        } finally {
            executor.shutdown();
        }
    }

    static class UserRolesCallback implements FutureCallback<UserRoles> {

        private final CountDownLatch latch;

        UserRolesCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onSuccess(UserRoles roles) {
            log.info("Roles received via FutureStub: {}", roles.getRolesBySiteMap());
            latch.countDown();
        }

        @Override
        public void onFailure(Throwable throwable) {
            log.error("Failed to get roles via FutureStub", throwable);
            latch.countDown();
        }
    }

    /**
     * Pide los roles de forma no bloqueante con Async Stub + StreamObserver.
     */
    static void getRolesWithObserver(UserServiceGrpc.UserServiceStub asyncStub, User user)
            throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        asyncStub.getRoles(user, new UserRolesObserver(latch));
        if (!latch.await(5, TimeUnit.SECONDS)) {
            log.error("Timed out waiting for roles via Async Stub");
        }
    }

    static class UserRolesObserver implements StreamObserver<UserRoles> {

        private final CountDownLatch latch;

        UserRolesObserver(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onNext(UserRoles roles) {
            log.info("Roles received via Async Stub: {}", roles.getRolesBySiteMap());
        }

        @Override
        public void onError(Throwable throwable) {
            log.error("Failed to get roles via Async Stub", throwable);
            latch.countDown();
        }

        @Override
        public void onCompleted() {
            latch.countDown();
        }
    }

}
