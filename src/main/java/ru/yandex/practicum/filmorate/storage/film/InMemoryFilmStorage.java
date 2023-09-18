package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private long counterId = 0L;

    @Override
    public Film addFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Название фильма было пустым");
            throw new ValidationException("Название фильма не может быть пустым!");
        }

        if (film.getDescription().length() > 200) {
            log.error("Превышена максимальная длина описания (200 символов)");
            throw new ValidationException("Превышена максимальная длина описания (200 символов)");
        }

        if (film.getReleaseDate().before(Date.valueOf("1895-12-28"))) {
            log.error("Нельзя добавлять даты ниже 28 декабря 1895 года");
            throw new ValidationException("Нельзя добавлять даты ниже 28 декабря 1895 года");
        }

        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        counterId++;
        film.setId(counterId);
        film.setLike(new HashSet<>());
        films.put(film.getId(), film);
        log.info("Создана объект нового фильма -> " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Объект с id " + film.getId() + " успешно обновлен.");
            return film;
        } else {
            log.error("Объект с id " + film.getId() + " не найден.");
            throw new NotFoundException("Объект с id " + film.getId() + " не найден.");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (!getFilmById(filmId).getLike().contains(userId)) {
            getFilmById(filmId).getLike().add(userId);
        } else
            throw new NotFoundException("Пользователь уже поставил лайк этому фильму.");
    }

    @Override
    public void deleteLike(Long userId, Long filmId) {
        if (getFilmById(filmId).getLike().contains(userId)) {
            getFilmById(filmId).getLike().remove(userId);
        } else
            throw new NotFoundException("Пользователь не ставил лайк этому фильму.");
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (films.containsKey(filmId)) {
            return films.get(filmId);
        } else
            throw new NotFoundException("Film not found.");
    }

    @Override
    public List<Film> getMostPopularFilm(int count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(film -> film.getLike().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}