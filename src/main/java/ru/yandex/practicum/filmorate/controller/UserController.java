package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    List<User> users = new ArrayList<>();
    @PostMapping("/user/create")
    void addUser(@Valid @RequestBody User user) throws ValidationException {
        if((user.getEmail() == null  || user.getEmail().equals("")) || user.getEmail().indexOf('@') == -1) {
            log.error("Электронная почта была пусто или не содержала символа '@'");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ '@'");
        }

        if (user.getLogin() == null  || user.getLogin().equals("") || user.getLogin().indexOf(' ') != -1) {
            log.error("логин не может быть пустым и содержать пробелы!");
            throw new ValidationException("логин не может быть пустым и содержать пробелы!");
        }

        if (user.getName() == null  || user.getName().equals("")) {
            log.warn("Поля имени у пользователя был вписан логин из-за пустого значения имени");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().after(new Date(System.currentTimeMillis()))) {
            log.error("дата рождения в будущем");
            throw new ValidationException("дата рождения не может быть в будущем!");
        }

        log.info("Был создан пользователь ->" +user);
        users.add(user);
    }

    @PutMapping("/user/update/{id}")
    void updateUser(@Valid @RequestBody User user,@PathVariable("id") Long id) {
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                users.set(i, user);
                found = true;
                break;
            }
        }

        if (found) {
            log.info("Объект с id " + id + " успешно обновлен.");
        } else {
            log.info("Объект с id " + id + " не найден.");
        }
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return users;
    }
}


