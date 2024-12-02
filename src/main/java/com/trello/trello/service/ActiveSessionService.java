package com.trello.trello.service;

import com.trello.trello.model.ActiveSession;
import com.trello.trello.model.User;
import com.trello.trello.repository.ActiveSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActiveSessionService {

    private final ActiveSessionRepository activeSessionRepository;

    public ActiveSessionService(ActiveSessionRepository activeSessionRepository) {
        this.activeSessionRepository = activeSessionRepository;
    }

    public ActiveSession createSession(String deviceInfo, String token, User user) {
        ActiveSession session = new ActiveSession();
        session.setDeviceInfo(deviceInfo);
        session.setToken(token);
        session.setLastActive(LocalDateTime.now());
        session.setUser(user);
        session.setCreatedAt(LocalDateTime.now());
        return activeSessionRepository.save(session);
    }

    public void updateLastActive(String token) {
        activeSessionRepository.findByToken(token).ifPresent(session -> {
            session.setLastActive(LocalDateTime.now());
            session.setUpdatedAt(LocalDateTime.now());
            activeSessionRepository.save(session);
        });
    }
    /**
     * Elimina una sesión activa basada en el token proporcionado.
     *
     * @param token El token de la sesión a eliminar.
     */
    @Transactional
    public void deleteSession(String token) {
        activeSessionRepository.deleteByToken(token);
    }

    public void deleteSessionsByUser(User user) {
        activeSessionRepository.deleteByUser(user);
    }

    public List<ActiveSession> getSessionsByUser(User user) {
        return activeSessionRepository.findByUser(user);
    }
}
