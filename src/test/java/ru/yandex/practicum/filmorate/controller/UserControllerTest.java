package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;

public class UserControllerTest {

    UserStorage userStorage;
    UserService userService;

    @Test
    void testAddUserValidEmail() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        UserController userController = new UserController(userStorage, userService);
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(Date.valueOf("1990-01-01"));

        Assertions.assertDoesNotThrow(() -> userController.addUser(user));
    }

    @Test
    void testAddUserInvalidEmail() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        UserController userController = new UserController(userStorage, userService);
        User user = new User();
        user.setEmail("testexample.com");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(Date.valueOf("1990-01-01"));

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
        Assertions.assertEquals("электронная почта не может быть пустой и должна содержать символ '@'", exception.getMessage());
    }

    @Test
    void testAddUserValidLogin() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        UserController userController = new UserController(userStorage, userService);
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(Date.valueOf("1990-01-01"));

        Assertions.assertDoesNotThrow(() -> userController.addUser(user));
    }

    @Test
    void testAddUserInvalidLogin() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        UserController userController = new UserController(userStorage, userService);
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test test");
        user.setName("Test User");
        user.setBirthday(Date.valueOf("1990-01-01"));

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
        Assertions.assertEquals("логин не может быть пустым и содержать пробелы!", exception.getMessage());
    }

    @Test
    void testAddUserEmptyName() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        UserController userController = new UserController(userStorage, userService);
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("");
        user.setBirthday(Date.valueOf("1990-01-01"));

        Assertions.assertDoesNotThrow(() -> userController.addUser(user));
        Assertions.assertEquals("test", user.getName());
    }

    @Test
    void testAddUserFutureBirthday() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        UserController userController = new UserController(userStorage, userService);
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(Date.valueOf("2100-01-01"));

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
        Assertions.assertEquals("дата рождения не может быть в будущем!", exception.getMessage());
    }
}