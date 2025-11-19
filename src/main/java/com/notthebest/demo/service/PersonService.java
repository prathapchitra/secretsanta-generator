package com.notthebest.demo.service;

import org.springframework.stereotype.Service;
import com.notthebest.demo.model.Person;
import com.notthebest.demo.repository.PersonRepo;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepo repo;

    public PersonService(PersonRepo repo) {
        this.repo = repo;
    }

    public Person getPerson(String id) {
        return repo.findById(id).orElse(null);
    }

    public void deletePerson(String id) {
        repo.deleteById(id);
    }

    public Person savePerson(Person person) {
        return repo.save(person);
    }

    public List<Person> getAllPersons() {
        return repo.findAll();
    }
}
