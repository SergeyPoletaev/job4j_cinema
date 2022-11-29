package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.enumeration.Cell;
import ru.job4j.cinema.enumeration.Row;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.SessionService;

import javax.servlet.http.HttpSession;

import static ru.job4j.cinema.util.HttpHelper.addUserToModel;

@ThreadSafe
@Controller
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/sessions")
    public String getSession(Model model, HttpSession httpSession) {
        addUserToModel(httpSession, model);
        model.addAttribute("films", sessionService.findAll());
        return "session/sessions";
    }

    @PostMapping("/selectSession")
    public String selectSession(@ModelAttribute Session session, HttpSession httpSession) {
        httpSession.setAttribute("session", session);
        return "redirect:/formRowSelection";
    }

    @GetMapping("/formRowSelection")
    public String rowSelection(Model model, HttpSession httpSession) {
        addUserToModel(httpSession, model);
        model.addAttribute("rows", Row.values());
        return "session/rowSelection";
    }

    @PostMapping("/selectRow")
    public String selectRow(@ModelAttribute Ticket ticket, HttpSession httpSession) {
        httpSession.setAttribute("row", ticket.getPosRow());
        return "redirect:/formCellSelection";
    }

    @GetMapping("/formCellSelection")
    public String cellSelection(Model model, HttpSession httpSession) {
        addUserToModel(httpSession, model);
        model.addAttribute("cells", Cell.values());
        return "session/cellSelection";
    }

    @PostMapping("/selectCell")
    public String selectCell(@ModelAttribute Ticket ticket, HttpSession httpSession) {
        httpSession.setAttribute("cell", ticket.getCell());
        return "redirect:/formBookingTicket";
    }
}
