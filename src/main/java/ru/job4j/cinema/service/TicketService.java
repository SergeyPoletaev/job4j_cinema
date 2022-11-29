package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketDbStore;

@ThreadSafe
@Service
public class TicketService {
    private final TicketDbStore ticketDbStore;

    public TicketService(TicketDbStore ticketDbStore) {
        this.ticketDbStore = ticketDbStore;
    }

    public boolean add(Ticket ticket) {
        return ticketDbStore.add(ticket);
    }
}
