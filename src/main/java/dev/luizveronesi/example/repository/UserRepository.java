package dev.luizveronesi.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.luizveronesi.example.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserCustomRepository {

	User findByEmail(String email);
}