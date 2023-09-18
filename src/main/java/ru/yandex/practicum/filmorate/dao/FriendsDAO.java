package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
public class FriendsDAO {
    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final UserDbStorage user;

    public FriendsDAO(JdbcTemplate jdbcTemplate, @Lazy UserDbStorage user) {
        this.jdbcTemplate = jdbcTemplate;
        this.user = user;
    }

    public List<User> getUserByIdFriends(Long userId) {
        LinkedList<User> friends = new LinkedList<>();
        String sql = "select FRIEND_ID from FRIENDS F" +
                " join USERS on id = F.USER_ID" +
                " where id = ?";
//                " where id = ? and F.IS_FRIENDS = TRUE";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId);
        while (sqlRowSet.next()) {
            friends.add(user.getUserById(sqlRowSet.getLong("FRIEND_ID")));
        }
        return friends;
    }

    public void addFriend(Long userId, Long friendId) {
        if(checkUserInTable(userId) && checkUserInTable(friendId)) {
            String sqlCheckStatus = "select IS_FRIENDS from FRIENDS" +
                    " where FRIEND_ID = ?  and USER_ID = ? ";
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlCheckStatus, friendId, userId);
            if(sqlRowSet.next()){
                if(!sqlRowSet.getBoolean("IS_FRIENDS")){
                    String sql1 = "update  FRIENDS" +
                            " set IS_FRIENDS = ? where FRIEND_ID = ?  and USER_ID = ?";
                    jdbcTemplate.update(sql1,true, friendId,userId);

                    String sql = "insert into FRIENDS (USER_ID,FRIEND_ID, IS_FRIENDS) values(?,?, TRUE)";
                    jdbcTemplate.update(sql, userId, friendId);
                }
            }else {
                String sql2 = "insert into FRIENDS (USER_ID,FRIEND_ID, IS_FRIENDS) values(?,?, FALSE)";
                jdbcTemplate.update(sql2, userId, friendId);
            }

            log.info("Добавление в друзья прошла успешно между пользователями с id -> " + userId + " и " + friendId);
        }else throw new NotFoundException("Не найден пользователь для добавлнеия в друщья");
    }

    public Boolean checkUserInTable(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE id = ?) as is_user";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()) {
            return sqlRowSet.getBoolean("is_user");
        }
        return false;
    }

    public void acceptAsFriends(Long userId, Long friendId) {
        String sqlCheckAccept = "SELECT *" +
                "FROM FRIENDS" +
                " WHERE user_id = ? AND friend_id = ? AND is_friends = false;";
        String sqlUpdateFriendsStatus = "UPDATE friends " +
                "SET is_friends = true" +
                " WHERE user_id = ? AND friend_id = ?;";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlCheckAccept, userId, friendId);

        if (sqlRowSet.next()) {
            jdbcTemplate.update(sqlUpdateFriendsStatus, userId, friendId);
        }else
            throw new NotFoundException("Пользователь у которого нужно обновть статус для добавления дружбы не найден в " +
                    "таблице FRIENDS. id пользователя ->"+ userId+"  id друга ->" + friendId);
    }

    public void deleteFriend(Long userId, Long friendId){
        String sql = "delete from FRIENDS where USER_ID = ? and FRIEND_ID =?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getMutualFriends(Long userId, Long otherId) {
        LinkedList<User> users = new LinkedList<>();
        String sql = "SELECT u.ID " +
                "FROM users u " +
                "JOIN friends f1 ON f1.friend_id = u.id " +
                "JOIN friends f2 ON f2.friend_id = u.id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, otherId);
        while (sqlRowSet.next()) {
            users.add(user.getUserById(sqlRowSet.getLong("id")));
        }
        return users;
    }
}
