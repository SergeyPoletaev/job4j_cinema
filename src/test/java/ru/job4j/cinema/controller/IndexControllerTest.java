package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class IndexControllerTest {

    @Test
    void whenGetIndex() {
        IndexController indexController = new IndexController();
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        String page = indexController.getIndex(model, httpSession);
        assertThat(page).isEqualTo("index");
    }
}