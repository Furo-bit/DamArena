package com.project.damarena.repository;

import com.project.damarena.model.Tournament;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TournamentRepository extends MongoRepository<Tournament, String> {
    List<Tournament> findAllByOrderByStartingTimeAsc();

    List<Tournament> findByEndingTimeBefore(LocalDateTime now);
    List<Tournament> findByStartingTimeBefore(LocalDateTime starting);

    Tournament findTournamentById(String tournamentId);
}
