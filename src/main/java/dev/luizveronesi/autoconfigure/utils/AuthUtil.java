package dev.luizveronesi.autoconfigure.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import dev.luizveronesi.autoconfigure.security.IUser;
import dev.luizveronesi.autoconfigure.security.UserProfile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUtil {

	private static final String ROLE_PREFIX = "ROLE_";

	public static UserProfile getProfile() {
		var user = getLogged();

		if (user == null
				|| user.getRoles() == null
				|| user.getRoles().isEmpty())
			throw new AuthenticationCredentialsNotFoundException("error.access.authorization");

		return UserProfile.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.picture(user.getPicture())
				.status(user.getStatus())
				.roles(user.getRoles())
				.build();
	}

	public static IUser getLogged() {
		var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof IUser)
			return (IUser) principal;
		return null;
	}

	public static String getAuthenticatedUserEmail() {
		var authentication = (IUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return authentication.getEmail();
	}

	public static List<String> getAuthenticatedUserRoles() {
		var authentication = (IUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return authentication.getRoles();
	}

	public static String getJwtToken() {
		return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
	}

	public static List<GrantedAuthority> getAuthorities(IUser user) {
		List<GrantedAuthority> granted = new ArrayList<>();

		for (var role : user.getRoles())
			granted.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));

		return granted;
	}
}