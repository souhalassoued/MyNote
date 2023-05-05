package com.MyNote.MyNote.repository;


import com.MyNote.MyNote.model.Employe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MyNoteRepository extends MongoRepository<Employe, String> {
    Optional<Employe> findByUsername(String username);
}