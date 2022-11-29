package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.SessionDbStore;

import java.util.List;

@ThreadSafe
@Service
public class SessionService {
    private final SessionDbStore sessionDbStore;

    public SessionService(SessionDbStore sessionDbStore) {
        this.sessionDbStore = sessionDbStore;
    }

    public List<Session> findAll() {
        return sessionDbStore.findAll();
    }
}
