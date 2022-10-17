package com.myhome.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myhome.models.Tutorial;

public interface TutorialRepository extends MongoRepository<Tutorial, String> {
	List<Tutorial> findByPublished(boolean published);

	List<Tutorial> findByTitleContaining(String title);
}
