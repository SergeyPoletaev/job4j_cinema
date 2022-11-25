package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static ru.job4j.cinema.util.HttpHelper.addUserToModel;

@ThreadSafe
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/formAddUser")
    public String addUser(Model model, HttpSession httpSession) {
        addUserToModel(httpSession, model);
        return "user/addUser";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (!userService.add(user)) {
            redirectAttributes.addFlashAttribute("message", "Пользователь с такой почтой уже существует");
            return "redirect:/formAddUser";
        }
        redirectAttributes.addFlashAttribute("message", "Пользователь успешно зарегистрирован");
        return "redirect:/formAddUser";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail, HttpSession session) {
        addUserToModel(session, model);
        model.addAttribute("fail", fail != null);
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession httpSession) {
        Optional<User> userDb = userService.findUserByEmailAndPhone(user.getEmail(), user.getPhone());
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        httpSession.setAttribute("user", userDb.get());
        return "redirect:/sessions";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}
