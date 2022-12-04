package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;

import java.util.List;

public interface TicketService {

    boolean add(Ticket ticket);

    List<Ticket> findAll();
}
