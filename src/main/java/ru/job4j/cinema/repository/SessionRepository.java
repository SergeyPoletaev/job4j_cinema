package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;

import java.util.List;

public interface SessionRepository {

    boolean add(Session session);

    List<Session> findAll();

}
