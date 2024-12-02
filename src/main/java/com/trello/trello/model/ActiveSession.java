package com.trello.trello.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "active_sessions")
public class ActiveSession extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String deviceInfo;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime lastActive;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
