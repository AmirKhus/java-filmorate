package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;

public class UserControllerTest {

    @Test
    void testAddUserValidEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(Date.valueOf("1990-01-01"));

        Assertions.assertDoesNotThrow(() -> userController.addUser(user));
    }

    @Test
    void testAddUserInvalidEmail() {
        UserController userController = new UserController();
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
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(Date.valueOf("1990-01-01"));

        Assertions.assertDoesNotThrow(() -> userController.addUser(user));
    }

    @Test
    void testAddUserInvalidLogin() {
        UserController userController = new UserController();
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
        UserController userController = new UserController();
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
        UserController userController = new UserController();
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test");
        user.setName("Test User");
        user.setBirthday(Date.valueOf("2100-01-01"));

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
        Assertions.assertEquals("дата рождения не может быть в будущем!", exception.getMessage());
    }
}