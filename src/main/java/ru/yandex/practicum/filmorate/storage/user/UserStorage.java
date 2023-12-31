package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriendsByUserId(Long userId);

    List<User> getMutualFriends(Long userId, Long otherId);
}
