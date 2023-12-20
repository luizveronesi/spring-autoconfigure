package dev.luizveronesi.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.luizveronesi.example.model.User;

public interface UserCustomRepository {

	// Example using pagination and including or removing fields
	Page<User> findByName(String name, String[] includes, String[] excludes, Pageable pageable);
}