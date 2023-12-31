package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;


@Qualifier
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final JdbcTemplate jdbcTemplate;
    private long counterId = 0L;
    private final LikesDAO likesDAO;
    private final FilmGenreDAO filmGenreDAO;
    private final MpaDAO mpaDAO;


    @Override
    public Film addFilm(Film film) {
        counterId++;
        film.setId(counterId);

        String sql = "insert into films (id,name,description,releaseDate,duration,RATE, MPA_id) values(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                counterId, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getMpa().getId());

        if (film.getGenres() != null) {
            film.setGenres(filmGenreDAO.deleteDuplicateFilmGenre(film.getGenres()));
            if (film.getGenres().size() == 1)
                filmGenreDAO.addGenreForFilm(film.getId(), film.getGenres().get(0).getId());
            else
                filmGenreDAO.addListGenresForFilm(film.getId(), film.getGenres());
        }

        log.info("Добавление новой строки в базу данных-> " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());

        String sqlUpdateFilm = "update films " +
                "set name = ?, description = ?, releaseDate = ?, duration = ?, RATE=?,  mpa_id = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlUpdateFilm,
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRate(),
                film.getMpa().getId(), film.getId());


        if (film.getGenres() != null) {
            film.setGenres(filmGenreDAO.deleteDuplicateFilmGenre(film.getGenres()));

            filmGenreDAO.deleteGenreForFilm(film.getId());

            for (Genre g : film.getGenres()) {
                filmGenreDAO.addGenreForFilm(film.getId(), g.getId());
            }

        }
        log.info("Объект с id " + film.getId() + " успешно обновлен.");
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = new LinkedList<>();
        String sql = "select * from films";
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet(sql);
        while (filmsRows.next()) {
            films.add(new Film(
                            filmsRows.getLong("id"),
                            filmsRows.getString("name"),
                            filmsRows.getString("description"),
                            Objects.requireNonNull(filmsRows.getDate("releaseDate")).toLocalDate(),
                            filmsRows.getInt("duration"),
                            filmsRows.getInt("rate"),
                            mpaDAO.getMpaById(filmsRows.getLong("mpa_id")),
                            new ArrayList<>(filmGenreDAO.getGenresByFilmId(filmsRows.getLong("id"))),
                            new HashSet<>(likesDAO.getLikeFilms(filmsRows.getLong("id")))
                    )
            );
        }

        return films;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        likesDAO.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        likesDAO.deleteLike(filmId, userId);
    }

    @Override
    public Film getFilmById(Long filmId) {
        String sql = "select * from films where id = ?";
        SqlRowSet filmSql = jdbcTemplate.queryForRowSet(sql, filmId);

        if (filmSql.next()) {
            System.out.println("ASDas");
            System.out.println(filmSql.getLong("id"));
            return new Film(
                    filmSql.getLong("id"),
                    filmSql.getString("name"),
                    filmSql.getString("description"),
                    Objects.requireNonNull(filmSql.getDate("releaseDate")).toLocalDate(),
                    filmSql.getInt("duration"),
                    filmSql.getInt("rate"),
                    mpaDAO.getMpaById(filmSql.getLong("mpa_id")),
                    new ArrayList<>(filmGenreDAO.getGenresByFilmId(filmSql.getLong("id"))),
                    new HashSet<>(likesDAO.getLikeFilms(filmSql.getLong("id")))
            );
        } else {
            log.info("Объект с id " + filmId + " не найден.");
            throw new NotFoundException("Объект с id " + filmId + " не найден.");
        }
    }

    @Override
    public List<Film> getMostPopularFilm(int count) {
        List<Film> topFilms = new LinkedList<>();

        String sql = "SELECT * " +
                "FROM films " +
                "ORDER BY rate DESC " +
                "LIMIT ?;";

//              String sql = "SELECT films.* FROM films " +
//                "JOIN likes ON likes.film_id = films.id " +
//                "GROUP BY films.id " +
//                "ORDER BY COUNT(likes.user_id) DESC " +
//                "LIMIT ?;";
        String sql2 = "select F.ID, F.NAME, F.DESCRIPTION, F.RELEASEDATE,  F.DURATION, F.RATE, F.MPA_ID " +
                "from FILMS  F LEFT JOIN  LIKES L on F.ID  = L.FILM_ID " +
                "GROUP BY F.ID, L.USER_ID ORDER BY COUNT(L.USER_ID) DESC LIMIT ?";
        SqlRowSet sqlTopFilms = jdbcTemplate.queryForRowSet(sql2, count);

        while (sqlTopFilms.next()) {
            topFilms.add(
                    new Film(
                            sqlTopFilms.getLong("id"),
                            sqlTopFilms.getString("name"),
                            sqlTopFilms.getString("description"),
                            Objects.requireNonNull(sqlTopFilms.getDate("releaseDate")).toLocalDate(),
                            sqlTopFilms.getInt("duration"),
                            sqlTopFilms.getInt("rate"),
                            mpaDAO.getMpaById(sqlTopFilms.getLong("mpa_id")),
                            new ArrayList<>(filmGenreDAO.getGenresByFilmId(sqlTopFilms.getLong("id"))),
                            new HashSet<>(likesDAO.getLikeFilms(sqlTopFilms.getLong("id")))
                    )
            );
        }

        return topFilms;
    }
}
