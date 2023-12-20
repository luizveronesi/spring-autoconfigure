package dev.luizveronesi.example.service;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import dev.luizveronesi.autoconfigure.security.IUser;
import dev.luizveronesi.autoconfigure.security.IUserService;
import dev.luizveronesi.example.enums.UserStatus;
import dev.luizveronesi.example.model.User;
import dev.luizveronesi.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

	private final UserRepository userRepository;

	@Override
	public IUser findForAuthentication(IUser user) {
		var persisted = userRepository.findByEmail(user.getEmail());
		if (persisted == null) {
			var newUser = User.builder()
					.uid(user.getId())
					.name(user.getName())
					.email(user.getEmail())
					.picture(user.getPicture())
					.status(UserStatus.VISITOR)
					.build();
			persisted = userRepository.save(newUser);
		}

		if (persisted.getStatus().equals(UserStatus.VISITOR))
			return null;

		var status = persisted.getStatus().name();
		if (persisted.getStatus().equals(UserStatus.SUPER))
			status = UserStatus.ADMIN.name();

		return IUser.builder()
				.id(persisted.getUid())
				.name(persisted.getName())
				.email(persisted.getEmail())
				.picture(persisted.getPicture())
				.status(persisted.getStatus().name())
				.roles(Arrays.asList(status))
				.build();
	}
}
