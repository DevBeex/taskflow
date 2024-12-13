package com.trello.trello.dto.board;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {
    private String name;
    private String description;
    private Long ownerId;

    public BoardRequestDto() {
    }

    public BoardRequestDto(String name, String description, Long ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }
}
