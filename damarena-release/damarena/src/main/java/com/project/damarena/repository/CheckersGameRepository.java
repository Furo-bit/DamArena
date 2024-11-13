package com.project.damarena.repository;

import com.project.damarena.model.CheckersGame;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckersGameRepository extends MongoRepository<CheckersGame, String> {

    @Query("{ $or: [ { 'player1.user.email': ?0, 'player2.user.email': ?1 }, { 'player1.user.email': ?1, 'player2.user.email': ?0 } ] }")
    CheckersGame findExistingGame(String email1, String email2);
}