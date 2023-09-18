package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDAO implements GenreStorage {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final JdbcTemplate jdbcTemplate;

    public List<Genre> getAllGenre() {
        List<Genre> genres = new ArrayList<>();
        String sql = "select * from GENRES";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()){
            genres.add(new Genre(sqlRowSet.getLong("id"),
                    sqlRowSet.getString("name")));
        }
        return genres;
    }

    public Genre getGenreById(Long id){
        String sql = "select * from GENRES where ID = ?";
        log.info("Возвращаем строку по id из таблицы GENRES!");
        return getGenreObject(jdbcTemplate.queryForRowSet(sql, id));
    }

    public Genre getGenreObject(SqlRowSet sqlRowSet) {
        if (sqlRowSet.next()) {
            return new Genre(
                    sqlRowSet.getLong("id"),
                    sqlRowSet.getString("name"));
        }throw new NotFoundException("Таблица GENRES пустая или в нем не найдена нужная строка");
    }
}
