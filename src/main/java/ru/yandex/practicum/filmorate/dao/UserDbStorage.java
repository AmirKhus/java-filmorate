package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Qualifier
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private long counterId = 0L;

    private final JdbcTemplate jdbcTemplate;
    private final FriendsDAO friendsDAO;

    @Override
    public User addUser(User user) {
        counterId++;
        user.setId(counterId);
        String sql = "insert into users(id, EMAIL,LOGIN, BIRTHDAY, NAME) values(?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getId(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getName());
        log.info("Был создан пользователь ->" + user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId());

        String sqlUpdateUser = "update USERS " +
                "set EMAIL = ?, LOGIN = ?, BIRTHDAY = ?, NAME = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlUpdateUser,
                user.getEmail(), user.getLogin(), user.getBirthday(), user.getName(), user.getId());
        log.info("Объект пользлваьедя с id " + user.getId() + " успешно обновлен.");


        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new LinkedList<>();

        String sql = "select * from USERS";
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet(sql);

        while (usersRows.next()) {
            users.add(new User(
                            usersRows.getLong("id"),
                            usersRows.getString("email"),
                            usersRows.getString("login"),
                            usersRows.getString("name"),
                            Objects.requireNonNull(usersRows.getDate("birthday")).toLocalDate()
                    )
            );
        }

        return users;
    }

    @Override
    public User getUserById(Long id) {
        String sql = "select * from users where id = ?";
        SqlRowSet userSql = jdbcTemplate.queryForRowSet(sql, id);

        if (userSql.next()) {
            return new User(
                    userSql.getLong("id"),
                    userSql.getString("email"),
                    userSql.getString("login"),
                    userSql.getString("name"),
                    Objects.requireNonNull(userSql.getDate("birthday")).toLocalDate()
            );
        } else {
            log.info("Объект с id " + id + " не найден.");
            throw new NotFoundException("Объект с id " + id + " не найден.");
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        friendsDAO.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        friendsDAO.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriendsByUserId(Long userId) {
        return friendsDAO.getUserByIdFriends(userId);
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherId) {
        return friendsDAO.getMutualFriends(userId, otherId);
    }
}
