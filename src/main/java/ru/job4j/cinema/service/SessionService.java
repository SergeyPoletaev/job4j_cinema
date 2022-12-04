package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;

import java.util.List;

public interface SessionService {

    boolean add(Session session);

    List<Session> findAll();
}
