package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikesDAO {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final JdbcTemplate jdbcTemplate;

    public void addLike(Long filmId,Long userId) {
        String sql = "insert into likes (film_id,user_id) values(?,?)";
        jdbcTemplate.update(sql, filmId, userId);

        sql = "select  * from LIKES";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        System.out.println(sql);
        while (sqlRowSet.next()) {
            System.out.println(sqlRowSet.getLong("film_id"));
            System.out.println(sqlRowSet.getLong("user_id"));
        }
    }

    public void deleteLike(Long filmId,Long userId) {
        if (checkUserInTable(userId)) {
            String sql = "delete from likes where film_id = ? and user_id =?";
            jdbcTemplate.update(sql, filmId, userId);
        }else throw new NotFoundException("Не найден пользователь для удаления лайка");
    }
    
    public List<Long> getLikeFilms(Long filmId){
        LinkedList<Long> userId = new LinkedList<>();
        String sql = "select USER_ID from LIKES " +
                "where FILM_ID = ?";


        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, filmId);
        System.out.println(sql);
        while (sqlRowSet.next()) {
            System.out.println(sqlRowSet.getLong("USER_ID"));
            userId.add(sqlRowSet.getLong("USER_ID"));
        }
        return userId;
    }

    public Boolean checkUserInTable(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE id = ?) as is_user";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()) {
            return sqlRowSet.getBoolean("is_user");
        }
        return false;
    }


}
