package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmGenreDAO {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final JdbcTemplate jdbcTemplate;

    public void addGenreForFilm(Long filmId, Long genreId) {
        String sqlCheck = "SELECT COUNT(*) as count " +
                "FROM film_genre " +
                "WHERE film_id = ? AND genre_id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlCheck, filmId, genreId);
        if (sqlRowSet.next()) {
            if (sqlRowSet.getLong("count") == 0) {
                String sql = "insert into FILM_GENRE(film_id, genre_id) values (?,?)";
                jdbcTemplate.update(sql, filmId, genreId);
            }
        }
    }

    public void addListGenresForFilm(Long filmId, List<Genre> genres) {
        String sqlCheck = "SELECT COUNT(*) as count " +
                "FROM film_genre " +
                "WHERE film_id = ? AND genre_id = ?;";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Genre genreId : genres) {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlCheck, filmId, genreId);
            if (sqlRowSet.next()) {
                if (sqlRowSet.getLong("count") == 0) {
                    Object[] args = {filmId, genreId};
                    batchArgs.add(args);
                    String sql = "insert into FILM_GENRE(film_id, genre_id) values (?,?)";
                    jdbcTemplate.batchUpdate(sql, batchArgs);
                }
            }
        }
    }

    public List<Genre> getGenresByFilmId(long filmId) {
        LinkedList<Genre> genres = new LinkedList<>();
        String sql = "SELECT g.id, g.name \n" +
                "FROM genres g \n" +
                "JOIN film_genre fg ON fg.genre_id = g.id \n" +
                "WHERE fg.film_id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, filmId);
        while (sqlRowSet.next()) {
            genres.add(
                    new Genre(
                            sqlRowSet.getLong("id"),
                            sqlRowSet.getString("name"))
            );
        }
        return genres;
    }

    public void deleteGenreForFilm(Long filmId) {
        String sql = "delete from FILM_GENRE where film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    public List<Genre> deleteDuplicateFilmGenre(List<Genre> genres) {
        List<Genre> uniqueList = new LinkedList<>();
        for (Genre element : genres) {
            if (!uniqueList.contains(element)) {
                uniqueList.add(element);
            }
        }

        return new ArrayList<>(uniqueList);
    }
}