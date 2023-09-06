package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    ResponseEntity<Object> updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User addFriend(Long userId, Long friendId);

    User deleteFriend(Long userId, Long friendId);

    List<User> getFriendsByUserId(Long userId);

    List<User> getMutualFriends(Long userId, Long otherId);
}
