package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.util.HttpHelper;

import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
public class IndexController {

    @GetMapping("/index")
    public String getIndex(Model model, HttpSession httpSession) {
        HttpHelper.addUserToModel(httpSession, model);
        return "index";
    }
}
