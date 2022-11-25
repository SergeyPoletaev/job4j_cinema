package ru.job4j.cinema.util;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.mockito.Mockito.*;

class HttpHelperTest {

    @Test
    void whenClearHttpSessionAttr() {
        List<String> attr = List.of("session", "row", "cell");
        HttpSession httpSession = mock(HttpSession.class);
        HttpHelper.clearHttpSessionAttr(httpSession, attr);
        verify(httpSession, times(1)).removeAttribute("session");
        verify(httpSession, times(1)).removeAttribute("row");
        verify(httpSession, times(1)).removeAttribute("cell");
    }

    @Test
    void whenAddUserToModelWithAuth() {
        User user = new User(1);
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        when((User) httpSession.getAttribute("user")).thenReturn(user);
        HttpHelper.addUserToModel(httpSession, model);
        verify(model).addAttribute("user", user);
    }

    @Test
    void whenAddUserToModelNotAuth() {
        User user = new User(0);
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        HttpHelper.addUserToModel(httpSession, model);
        verify(model).addAttribute("user", user);
    }
}