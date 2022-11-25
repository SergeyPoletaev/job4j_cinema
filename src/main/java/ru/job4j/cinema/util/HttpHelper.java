package ru.job4j.cinema.util;

import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public class HttpHelper {

    private HttpHelper() {

    }

    public static void clearHttpSessionAttr(HttpSession httpSession, List<String> attr) {
        attr.forEach(httpSession::removeAttribute);
    }

    public static void addUserToModel(HttpSession httpSession, Model model) {
        model.addAttribute("user", getUser(httpSession));
    }

    private static User getUser(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        return user;
    }
}
