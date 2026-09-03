package ar.edu.itba.pod.grpc.server;

import ar.edu.itba.pod.grpc.user.AccountStatus;
import ar.edu.itba.pod.grpc.user.LoginInformation;
import ar.edu.itba.pod.grpc.user.Role;
import ar.edu.itba.pod.grpc.user.User;
import ar.edu.itba.pod.grpc.user.UserRoles;
import ar.edu.itba.pod.grpc.user.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import module java.base;

@Service
public class Servant extends UserServiceGrpc.UserServiceImplBase {

	/**
	 * Sample Data
	 */
	private static final Map<String, String> passwordsByUser = Map.of(
			"foo", "foopass",
			"bar", "barpass");
	private static final Map<String, User> users = Map.of(
			"foo", User.newBuilder()
					.setUserName("foo")
					.setDisplayName("Foo")
					.setStatus(AccountStatus.ACCOUNT_STATUS_ACTIVE)
					.addPreferences("darkMode")
					.addPreferences("liteView").build(),
			"bar", User.newBuilder()
					.setUserName("bar")
					.setDisplayName("Bar")
					.setStatus(AccountStatus.ACCOUNT_STATUS_PENDING)
					.addPreferences("lightMode").build());
	private final Map<String, UserRoles> userRolesMap = Map.of(
			"foo", UserRoles.newBuilder()
					.putRolesBySite("abc.com", Role.ADMIN)
					.putRolesBySite("xyz.com", Role.BUYER).build(),
			"bar", UserRoles.newBuilder()
					.putRolesBySite("abc.com", Role.SELLER).build());

	@Override
	public void doLogin(LoginInformation request, StreamObserver<User> responseObserver) {
		String expectedPassword = passwordsByUser.get(request.getUserName());
		if (expectedPassword == null || !expectedPassword.equals(request.getPassword())) {
			responseObserver.onError(Status.UNAUTHENTICATED
					.withDescription("Invalid user or password")
					.asRuntimeException());
			return;
		}
		responseObserver.onNext(users.get(request.getUserName()));
		responseObserver.onCompleted();
	}

	@Override
	public void getRoles(User request, StreamObserver<UserRoles> responseObserver) {
		UserRoles roles = userRolesMap.get(request.getUserName());
		if (roles == null) {
			responseObserver.onError(Status.NOT_FOUND
					.withDescription("Unknown user")
					.asRuntimeException());
			return;
		}
		responseObserver.onNext(roles);
		responseObserver.onCompleted();
	}

}
