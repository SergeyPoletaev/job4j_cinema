package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.Optional;

@ThreadSafe
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean add(User user) {
        return userRepository.add(user);
    }

    @Override
    public Optional<User> findUserByEmailAndPhone(String email, String phone) {
        return userRepository.findUserByEmailAndPhone(email, phone);
    }
}
