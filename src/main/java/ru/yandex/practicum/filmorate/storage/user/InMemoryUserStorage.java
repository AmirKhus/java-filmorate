package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Long, User> users = new HashMap<>();
    private long counterId = 0L;

    @Override
    public User addUser(User user) {
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

        counterId++;
        user.setId(counterId);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("Был создан пользователь ->" + user);
        return user;
    }

    @Override
    public ResponseEntity<Object> updateUser(User user) {
        if (users.containsKey(user.getId())) {
            if (user.getFriends() == null)
                user.setFriends(new HashSet<>());

            users.put(user.getId(), user);
            log.info("Объект с id " + user.getId() + " успешно обновлен.");
            return ResponseEntity.ok(user);
        } else {
            log.info("Объект с id " + user.getId() + " не найден.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
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
    public User addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);


        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.error("Взаимное доавление в друзья прошла успешна между пользователями с id -> " + userId + " и " + friendId);
        return getUserById(userId);
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        getUserById(userId).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(userId);
        return getUserById(userId);
    }

    @Override
    public List<User> getFriendsByUserId(Long userId) {
        List<User> usersFriendsList = new LinkedList<>();
        for (Long userById :
                getUserById(userId).getFriends()) {
            usersFriendsList.add(getUserById(userById));
        }
        return usersFriendsList;
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long friendId) {
        List<User> mutualFriends = new ArrayList<>();
        for (Long id : getUserById(userId).getFriends()) {
            if (getUserById(friendId).getFriends().contains(id)) {
                mutualFriends.add(getUserById(id));
            }
        }
        return mutualFriends;
    }


}
