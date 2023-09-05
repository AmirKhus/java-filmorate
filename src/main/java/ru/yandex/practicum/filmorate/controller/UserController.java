package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Long, User> users = new HashMap<>();
    private long counterId = 0L;

    @PostMapping
    public @Valid User addUser(@Valid @RequestBody User user) {
        counterId++;
        user.setId(counterId);

        if ((user.getEmail() == null || user.getEmail().equals("")) || user.getEmail().indexOf('@') == -1) {
            log.error("Электронная почта была пусто или не содержала символа '@'");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ '@'");
        }

        if (user.getLogin() == null || user.getLogin().equals("") || user.getLogin().indexOf(' ') != -1) {
            log.error("логин не может быть пустым и содержать пробелы!");
            throw new ValidationException("логин не может быть пустым и содержать пробелы!");
        }

        if (user.getName() == null || user.getName().equals("")) {
            log.warn("Поля имени у пользователя был вписан логин из-за пустого значения имени");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().after(new Date(System.currentTimeMillis()))) {
            log.error("дата рождения в будущем");
            throw new ValidationException("дата рождения не может быть в будущем!");
        }

        log.info("Был создан пользователь ->" + user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public ResponseEntity<Object> updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Объект с id " + user.getId() + " успешно обновлен.");
            return ResponseEntity.ok(user);
        } else {
            log.info("Объект с id " + user.getId() + " не найден.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}


