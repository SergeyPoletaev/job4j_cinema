package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.util.HttpHelper;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @Test
    void bookingTicket() {
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        Session session = new Session(1, "Zvezda");
        HttpSession httpSession = mock(HttpSession.class);
        when((Session) httpSession.getAttribute("session")).thenReturn(session);
        when(httpSession.getAttribute("row")).thenReturn(2);
        when(httpSession.getAttribute("cell")).thenReturn(3);
        Model model = mock(Model.class);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            page = ticketController.bookingTicket(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        verify(model).addAttribute("film", session.getName());
        verify(model).addAttribute("row", 2);
        verify(model).addAttribute("cell", 3);
        assertThat(page).isEqualTo("ticket/booking");
    }

    @Test
    void whenCancelBooking() {
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            HttpSession httpSession = mock(HttpSession.class);
            page = ticketController.cancelBooking(httpSession);
            List<String> attr = List.of("session", "row", "cell");
            httpHelper.verify(() -> HttpHelper.clearHttpSessionAttr(httpSession, attr));
        }
        assertThat(page).isEqualTo("redirect:cancel");
    }

    @Test
    void whenGetCanselPage() {
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            HttpSession httpSession = mock(HttpSession.class);
            Model model = mock(Model.class);
            page = ticketController.getCanselPage(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        assertThat(page).isEqualTo("info/cancel");
    }

    @Test
    void whenConfirmBookingThenSuccess() {
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        HttpSession httpSession = mock(HttpSession.class);
        Session session = new Session();
        User user = new User();
        when((Session) httpSession.getAttribute("session")).thenReturn(session);
        when(httpSession.getAttribute("row")).thenReturn(2);
        when(httpSession.getAttribute("cell")).thenReturn(3);
        when((User) httpSession.getAttribute("user")).thenReturn(user);
        Ticket ticket = new Ticket(4, 2, 3, 5);
        when(ticketService.add(ticket)).thenReturn(true);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            Model model = mock(Model.class);
            page = ticketController.confirmBooking(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
            List<String> attr = List.of("session", "row", "cell");
            httpHelper.verify(() -> HttpHelper.clearHttpSessionAttr(httpSession, attr));
        }
        verify(ticketService).add(ticket);
        assertThat(page).isEqualTo("redirect:/success");
    }

    @Test
    void whenConfirmBookingThenFail() {
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        HttpSession httpSession = mock(HttpSession.class);
        Session session = new Session();
        User user = new User();
        when((Session) httpSession.getAttribute("session")).thenReturn(session);
        when(httpSession.getAttribute("row")).thenReturn(2);
        when(httpSession.getAttribute("cell")).thenReturn(3);
        when((User) httpSession.getAttribute("user")).thenReturn(user);
        Ticket ticket = new Ticket(4, 2, 3, 5);
        when(ticketService.add(ticket)).thenReturn(false);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            Model model = mock(Model.class);
            page = ticketController.confirmBooking(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
            List<String> attr = List.of("session", "row", "cell");
            httpHelper.verify(() -> HttpHelper.clearHttpSessionAttr(httpSession, attr));
        }
        verify(ticketService).add(ticket);
        assertThat(page).isEqualTo("redirect:/fail");
    }

    @Test
    void whenGetSuccessPage() {
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            Model model = mock(Model.class);
            HttpSession httpSession = mock(HttpSession.class);
            page = ticketController.getSuccessPage(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        assertThat(page).isEqualTo("info/success");
    }

    @Test
    void whenGetFailPage() {
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            Model model = mock(Model.class);
            HttpSession httpSession = mock(HttpSession.class);
            page = ticketController.getFailPage(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        assertThat(page).isEqualTo("info/fail");
    }
}