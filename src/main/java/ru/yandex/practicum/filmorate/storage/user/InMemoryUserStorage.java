package ru.yandex.practicum.filmorate.storage.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Long, User> users = new HashMap<>();
    private long counterId = 0L;

    @Override
    public User addUser(User user) {
        if ((user.getEmail() == null || user.getEmail().isEmpty()) || user.getEmail().indexOf('@') == -1) {
            log.error("Электронная почта была пусто или не содержала символа '@'");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ '@'");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().indexOf(' ') != -1) {
            log.error("логин не может быть пустым и содержать пробелы!");
            throw new ValidationException("логин не может быть пустым и содержать пробелы!");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            log.warn("Поля имени у пользователя был вписан логин из-за пустого значения имени");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().after(new Date(System.currentTimeMillis()))) {
            log.error("дата рождения в будущем");
            throw new ValidationException("дата рождения не может быть в будущем!");
        }

        counterId++;
        user.setId(counterId);
        users.put(user.getId(), user);
        log.info("Был создан пользователь ->" + user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {


            users.put(user.getId(), user);
            log.info("Объект с id " + user.getId() + " успешно обновлен.");
            return user;
        } else {
            log.error("Объект с id " + user.getId() + " не найден.");
            throw new NotFoundException("Объект с id " + user.getId() + " не найден.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else
            throw new NotFoundException("Не найден пользователь с id-> " + id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        log.error("Взаимное доавление в друзья прошла успешна между пользователями с id -> " + userId + " и " + friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
    }

    @Override
    public List<User> getFriendsByUserId(Long userId) {
        Set<User> set = new TreeSet<>();
        List<User> usersFriendsList = new ArrayList<>(set);

        return usersFriendsList;
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long friendId) {
        List<User> mutualFriends = new ArrayList<>();
        return mutualFriends;
    }


}


