package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film addLike(Long userId, Long filmId) {
        filmStorage.addLike(userId, filmId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(Long userId, Long filmId) {
        filmStorage.deleteLike(userId, filmId);
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getMostPopularFilm(int count) {
        return filmStorage.getMostPopularFilm(count);
    }
}
