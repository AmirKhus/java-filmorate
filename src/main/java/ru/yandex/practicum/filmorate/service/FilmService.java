package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film addLike(Long filmId,Long userId) {
        filmStorage.addLike(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(Long filmId, Long userId) {
        filmStorage.deleteLike(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getMostPopularFilm(int count) {
        return filmStorage.getMostPopularFilm(count);
    }
}
