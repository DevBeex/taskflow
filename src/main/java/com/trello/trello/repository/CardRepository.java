package com.trello.trello.repository;

import com.trello.trello.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByListId(Long tasklistId);
}
