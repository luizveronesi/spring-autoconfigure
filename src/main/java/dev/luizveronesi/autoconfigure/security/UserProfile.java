package dev.luizveronesi.autoconfigure.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class UserProfile {

	private String id;
	private String name;
	private String email;
	private String picture;
	private String status;
	private List<String> roles;
}