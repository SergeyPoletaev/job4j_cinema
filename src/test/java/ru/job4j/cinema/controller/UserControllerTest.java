package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.util.HttpHelper;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void whenAddUser() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            Model model = mock(Model.class);
            HttpSession httpSession = mock(HttpSession.class);
            page = userController.addUser(model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        assertThat(page).isEqualTo("user/addUser");
    }

    @Test
    void whenRegistrationThenSuccess() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        User user = new User(1);
        when(userService.add(user)).thenReturn(true);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String page = userController.registration(user, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("message", "Пользователь успешно зарегистрирован");
        assertThat(page).isEqualTo("redirect:/formAddUser");
    }

    @Test
    void whenRegistrationThenFail() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        User user = new User(1);
        when(userService.add(user)).thenReturn(false);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String page = userController.registration(user, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("message", "Пользователь с такой почтой уже существует");
        assertThat(page).isEqualTo("redirect:/formAddUser");
    }

    @Test
    void whenLoginPage() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        Model model = mock(Model.class);
        boolean fail = true;
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            HttpSession httpSession = mock(HttpSession.class);
            page = userController.loginPage(model, fail, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        verify(model).addAttribute("fail", fail);
        assertThat(page).isEqualTo("user/login");
    }

    @Test
    void whenLoginThenSuccess() {
        UserService userService = mock(UserService.class);
        User user = new User(1, "anna", "anna@", "123");
        when(userService.findUserByEmailAndPhone("anna@", "123")).thenReturn(Optional.of(user));
        UserController userController = new UserController(userService);
        HttpSession httpSession = mock(HttpSession.class);
        String page = userController.login(user, httpSession);
        verify(httpSession).setAttribute("user", user);
        assertThat(page).isEqualTo("redirect:/sessions");
    }

    @Test
    void whenLoginThenFail() {
        UserService userService = mock(UserService.class);
        User user = new User(1, "anna", "anna@", "123");
        when(userService.findUserByEmailAndPhone("anna@", "123")).thenReturn(Optional.empty());
        UserController userController = new UserController(userService);
        HttpSession httpSession = mock(HttpSession.class);
        String page = userController.login(user, httpSession);
        assertThat(page).isEqualTo("redirect:/loginPage?fail=true");
    }

    @Test
    void whenLogout() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        HttpSession httpSession = mock(HttpSession.class);
        String page = userController.logout(httpSession);
        verify(httpSession).invalidate();
        assertThat(page).isEqualTo("redirect:/loginPage");
    }
}