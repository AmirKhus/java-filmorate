package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    void addLike(Long filmId,Long userId);

    void deleteLike(Long userId, Long filmId);

    Film getFilmById(Long filmId);

    List<Film> getMostPopularFilm(int count);
}
