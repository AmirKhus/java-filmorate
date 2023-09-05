package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private long counterId = 0L;

    @PostMapping("/film/create")
    public void addFilm(@Valid @RequestBody Film film) {
        counterId++;
        film.setId(counterId);
        if (film.getName() == null || film.getName().equals("")) {
            log.error("Название фильма было пустым");
            throw new ValidationException("Название фильма не может быть пустым!");
        }

        if (film.getDescription().length() > 200) {
            log.error("Превышена максимальная длина описания (200 символов)");
            throw new ValidationException("Превышена максимальная длина описания (200 символов)");
        }

        if (film.getReleaseDate().compareTo(Date.valueOf("1895-12-28")) < 0) {
            log.error("Нельзя добавлять даты ниже 28 декабря 1895 года");
            throw new ValidationException("Нельзя добавлять даты ниже 28 декабря 1895 года");
        }

        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        log.info("Создана объект нового фильма -> " + film);
        films.put(counterId, film);
    }

    @PutMapping("/film/update/{id}")
    public void updateFilm(@Valid @RequestBody Film film, @PathVariable("id") Long id) {
        if (films.containsKey(id)) {
            film.setId(id);
            films.put(id, film);
            log.info("Объект с id " + id + " успешно обновлен.");
        } else {
            log.info("Объект с id " + id + " не найден.");
        }
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
