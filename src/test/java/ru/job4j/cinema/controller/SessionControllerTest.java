package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.ui.Model;
import ru.job4j.cinema.enums.Cell;
import ru.job4j.cinema.enums.Row;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.util.HttpHelper;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @Test
    void whenGetSession() {
        List<Session> sessions = List.of(
                new Session(1, "msg1"),
                new Session(2, "msg2")
        );
        SessionService sessionService = mock(SessionService.class);
        when(sessionService.findAll()).thenReturn(sessions);
        SessionController sessionController = new SessionController(sessionService);
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            page = sessionController.getSession(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        verify(model).addAttribute("films", sessions);
        assertThat(page).isEqualTo("session/sessions");
    }

    @Test
    void whenSelectSession() {
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        HttpSession httpSession = mock(HttpSession.class);
        Session session = new Session(1, "msg");
        String page = sessionController.selectSession(session, httpSession);
        verify(httpSession).setAttribute("session", session);
        assertThat(page).isEqualTo("redirect:/formRowSelection");
    }

    @Test
    void whenRowSelection() {
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            page = sessionController.rowSelection(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        verify(model).addAttribute("rows", Row.values());
        assertThat(page).isEqualTo("session/rowSelection");
    }

    @Test
    void whenSelectRow() {
        SessionService sessionService = mock(SessionService.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionController sessionController = new SessionController(sessionService);
        Ticket ticket = new Ticket(1, new Session(), 2, 3, new User());
        String page = sessionController.selectRow(ticket, httpSession);
        verify(httpSession).setAttribute("row", ticket.getPosRow());
        assertThat(page).isEqualTo("redirect:/formCellSelection");
    }

    @Test
    void whenCellSelection() {
        SessionService sessionService = mock(SessionService.class);
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionController sessionController = new SessionController(sessionService);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            page = sessionController.cellSelection(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        verify(model).addAttribute("cells", Cell.values());
        assertThat(page).isEqualTo("session/cellSelection");
    }

    @Test
    void whenSelectCell() {
        SessionService sessionService = mock(SessionService.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionController sessionController = new SessionController(sessionService);
        Ticket ticket = new Ticket(1, new Session(), 2, 3, new User());
        String page = sessionController.selectCell(ticket, httpSession);
        verify(httpSession).setAttribute("cell", ticket.getCell());
        assertThat(page).isEqualTo("redirect:/formBookingTicket");
    }
}