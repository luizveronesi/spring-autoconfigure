package dev.luizveronesi.example.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.luizveronesi.example.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// add your collection name
@Document(collection = "services.user")
@Getter
@Setter
@Builder
public class User implements Serializable {

	@Id
	private String id;

	private String uid;
	private String name;
	private String login;
	private String email;
	private String picture;
	private String expiration;
	private UserStatus status;
}
