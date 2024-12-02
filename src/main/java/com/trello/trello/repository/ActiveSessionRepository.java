package com.trello.trello.repository;

import com.trello.trello.model.ActiveSession;
import com.trello.trello.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActiveSessionRepository extends JpaRepository<ActiveSession, Long> {
    Optional<ActiveSession> findByToken(String token);
    List<ActiveSession> findByUser(User user);
    void deleteByToken(String token);
    void deleteByUser(User user);
}