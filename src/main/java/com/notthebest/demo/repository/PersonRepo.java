package com.notthebest.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.notthebest.demo.model.Person;

public interface PersonRepo extends MongoRepository<Person, String> {
}
