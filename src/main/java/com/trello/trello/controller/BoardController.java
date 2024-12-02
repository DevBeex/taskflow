package com.trello.trello.controller;

import com.trello.trello.constants.ErrorMessages;
import com.trello.trello.dto.board.BoardRequestDto;
import com.trello.trello.model.Board;
import com.trello.trello.model.User;
import com.trello.trello.repository.BoardRepository;
import com.trello.trello.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public BoardController(UserRepository userRepository, BoardRepository boardRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    @PostMapping
    public ResponseEntity<Board> createBoard(@Valid @RequestBody BoardRequestDto board) {
        try {
            Board newBoard = new Board();
            newBoard.setName(board.getName());
            newBoard.setDescription(board.getDescription());
            User user = userRepository.findById(board.getOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.USER_NOT_FOUND));
            newBoard.setUser(user);
            return ResponseEntity.ok(boardRepository.save(newBoard));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
