package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Qualifier
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private long counterId = 0L;

    private final JdbcTemplate jdbcTemplate;
    private final FriendsDAO friendsDAO;

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
                            usersRows.getDate("birthday")
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
                    userSql.getDate("birthday")
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
