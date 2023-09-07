package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final UserService userService;

    @PostMapping
    public @Valid User addUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.getAllUsers());
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userStorage.getUserById(Long.parseLong(id));
    }


    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable String id, @PathVariable String friendId) {
        return userService.addFriend(Long.parseLong(id), Long.parseLong(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        userService.deleteFriend(Long.parseLong(id), Long.parseLong(friendId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable String id) {
        return userService.getUserFriends(Long.parseLong(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable String id, @PathVariable String otherId) {
        return userService.getMutualFriends(Long.parseLong(id), Long.parseLong(otherId));
    }
}


