package com.trello.trello.repository;

import com.trello.trello.model.Tasklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasklistRepository extends JpaRepository<Tasklist, Long> {
    List<Tasklist> findByBoardId(Long boardId);
}
