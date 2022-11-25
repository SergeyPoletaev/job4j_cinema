package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.List;

import static ru.job4j.cinema.util.HttpHelper.addUserToModel;
import static ru.job4j.cinema.util.HttpHelper.clearHttpSessionAttr;

@ThreadSafe
@Controller
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/formBookingTicket")
    public String bookingTicket(Model model, HttpSession httpSession) {
        addUserToModel(httpSession, model);
        Session session = (Session) httpSession.getAttribute("session");
        model.addAttribute("film", session.getName());
        model.addAttribute("row", httpSession.getAttribute("row"));
        model.addAttribute("cell", httpSession.getAttribute("cell"));
        return "ticket/booking";
    }

    @PostMapping("/cancelBooking")
    public String cancelBooking(HttpSession httpSession) {
        clearHttpSessionAttr(httpSession, List.of("session", "row", "cell"));
        return "redirect:cancel";
    }

    @GetMapping("/cancel")
    public String getCanselPage(Model model, HttpSession httpSession) {
        addUserToModel(httpSession, model);
        return "info/cancel";
    }

    @PostMapping("/bookTicket")
    public String confirmBooking(Model model, HttpSession httpSession) {
        addUserToModel(httpSession, model);
        Ticket ticket = new Ticket(
                (Session) httpSession.getAttribute("session"),
                (Integer) httpSession.getAttribute("row"),
                (Integer) httpSession.getAttribute("cell"),
                (User) httpSession.getAttribute("user")
        );
        if (ticketService.add(ticket)) {
            clearHttpSessionAttr(httpSession, List.of("session", "row", "cell"));
            return "redirect:/success";
        }
        clearHttpSessionAttr(httpSession, List.of("session", "row", "cell"));
        return "redirect:/fail";
    }

    @GetMapping("/success")
    public String getSuccessPage(Model model, HttpSession httpSession) {
        addUserToModel(httpSession, model);
        return "info/success";
    }

    @GetMapping("/fail")
    public String getFailPage(Model model, HttpSession httpSession) {
        addUserToModel(httpSession, model);
        return "info/fail";
    }
}
