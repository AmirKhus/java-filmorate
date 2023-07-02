package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {
    List<Film> films = new ArrayList<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping("/film/create")
    void addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if(film.getName() == null  || film.getName().equals("")) {
            log.error("Название фильма было пустым");
            throw new ValidationException("Название фильма не может быть пустым!");
        }

        if (film.getDescription().length()>200) {
            log.error("Превышена максимальная длина описания (200 символов)");
            throw new ValidationException("Превышена максимальная длина описания (200 символов)");
        }

        if (film.getReleaseDate().compareTo(Date.valueOf("1895-12-28")) < 0) {
            log.error("Нельзя добавлять даты ниже 28 декабря 1895 года");
            throw new ValidationException("Нельзя добавлять даты ниже 28 декабря 1895 года");
        }

        if (film.getDuration()<0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        log.info("Создана объект нового фильма -> "+film);
        films.add(film);
    }

    @PutMapping("/film/update/{id}")
    void updateFilm(@Valid @RequestBody Film film, @PathVariable("id") Long id) {
        log.info("Изменен объект с id "+ film.getId()+". Обновленные данные фильма  -> "+film);

        boolean found = false;
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId() == id) {
                films.set(i, film);
                found = true;
                break;
            }
        }

        if (found) {
            log.info("Объект с id " + id + " успешно обновлен.");
        } else {
            log.info("Объект с id " + id + " не найден.");
        }
    }

    @GetMapping("/films")
    List<Film> getAllFilms() {
        return films;
    }
}
