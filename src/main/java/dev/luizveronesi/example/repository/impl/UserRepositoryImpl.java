package dev.luizveronesi.example.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import dev.luizveronesi.example.model.User;
import dev.luizveronesi.example.repository.UserCustomRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserCustomRepository {

	private final MongoTemplate mongoTemplate;

	public Page<User> findByName(
			String name,
			String[] includes,
			String[] excludes,
			Pageable pageable) {

		var query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		query.with(pageable);

		if (excludes != null)
			query.fields().exclude(excludes);

		if (includes != null)
			query.fields().include(includes);

		return PageableExecutionUtils.getPage(
				mongoTemplate.find(query, User.class),
				pageable,
				() -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class));
	}
}
