package dev.luizveronesi.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luizveronesi.autoconfigure.security.UserProfile;
import dev.luizveronesi.autoconfigure.utils.AuthUtil;
import dev.luizveronesi.example.controller.documentation.UserControllerDocumentation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController implements UserControllerDocumentation {

	@GetMapping(value = "/profile")
	public ResponseEntity<UserProfile> getLogged() {
		return new ResponseEntity<>(AuthUtil.getProfile(), HttpStatus.OK);
	}
}