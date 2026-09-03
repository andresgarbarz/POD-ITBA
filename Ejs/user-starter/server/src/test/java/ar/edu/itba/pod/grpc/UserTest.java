package ar.edu.itba.pod.grpc;

import ar.edu.itba.pod.grpc.server.HealthConfig;
import ar.edu.itba.pod.grpc.server.Servant;
import ar.edu.itba.pod.grpc.user.AccountStatus;
import ar.edu.itba.pod.grpc.user.LoginInformation;
import ar.edu.itba.pod.grpc.user.Role;
import ar.edu.itba.pod.grpc.user.User;
import ar.edu.itba.pod.grpc.user.UserRoles;
import ar.edu.itba.pod.grpc.user.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.grpc.test.autoconfigure.AutoConfigureInProcessTransport;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.grpc.client.ImportGrpcClients;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@SpringJUnitConfig(UserTest.TestConfig.class)
@AutoConfigureInProcessTransport
public class UserTest {
	@Autowired
	private UserServiceGrpc.UserServiceBlockingStub stub;

	@Test
	void loginTest() {
		User user = stub.doLogin(LoginInformation.newBuilder()
				.setUserName("foo")
				.setPassword("foopass")
				.build());

		assertEquals("foo", user.getUserName());
		assertEquals("Foo", user.getDisplayName());
		assertEquals(AccountStatus.ACCOUNT_STATUS_ACTIVE, user.getStatus());
		assertTrue(user.getPreferencesList().contains("darkMode"));
	}

	@Test
	void loginRejectsInvalidPassword() {
		StatusRuntimeException ex = assertThrows(StatusRuntimeException.class,
				() -> stub.doLogin(LoginInformation.newBuilder()
						.setUserName("foo")
						.setPassword("wrong")
						.build()));
		assertEquals(Status.UNAUTHENTICATED.getCode(), ex.getStatus().getCode());
	}

	@Test
	void getRolesTest() {
		User user = stub.doLogin(LoginInformation.newBuilder()
				.setUserName("bar")
				.setPassword("barpass")
				.build());

		UserRoles roles = stub.getRoles(user);

		assertEquals(Role.SELLER, roles.getRolesBySiteMap().get("abc.com"));
	}

	@EnableAutoConfiguration
	@Import({ Servant.class, HealthConfig.class })
	@ImportGrpcClients(types = UserServiceGrpc.UserServiceBlockingStub.class)
	static class TestConfig {
	}

}
