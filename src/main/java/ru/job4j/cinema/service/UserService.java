package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.persistence.UserDbStore;

import java.util.Optional;

@ThreadSafe
@Service
public class UserService {
    private final UserDbStore userDbStore;

    public UserService(UserDbStore userDbStore) {
        this.userDbStore = userDbStore;
    }

    public boolean add(User user) {
        return userDbStore.add(user);
    }

    public Optional<User> findUserByEmailAndPhone(String email, String phone) {
        return userDbStore.findUserByEmailAndPhone(email, phone);
    }
}
