package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

public class FilmControllerTest {

    FilmStorage filmStorage;
    FilmService filmService;

    @Test
    void testAddFilmValidName() throws ValidationException {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmStorage, filmService);
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film");
        film.setReleaseDate(LocalDate.parse("2023-06-28"));
        film.setDuration(120);

        Assertions.assertDoesNotThrow(() -> filmController.addFilm(film));
    }

    @Test
    void testAddFilmEmptyName() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmStorage, filmService);
        Film film = new Film();
        film.setName("");
        film.setDescription("This is a test film");
        film.setReleaseDate(LocalDate.parse("2023-06-28"));
        film.setDuration(120);

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        Assertions.assertEquals("Название фильма не может быть пустым!", exception.getMessage());
    }

    @Test
    void testAddFilmExceededDescriptionLength() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmStorage, filmService);
        Film film = new Film();
        film.setName("Тестовый фильм");
        film.setDescription("A".repeat(201));
        film.setReleaseDate(LocalDate.parse("2023-06-28"));
        film.setDuration(120);

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        Assertions.assertEquals("Превышена максимальная длина описания (200 символов)", exception.getMessage());
    }

    @Test
    void testAddFilmInvalidReleaseDate() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmStorage, filmService);
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film");
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        film.setDuration(120);

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        Assertions.assertEquals("Нельзя добавлять даты ниже 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void testAddFilmNegativeDuration() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmStorage, filmService);
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film");
        film.setReleaseDate(LocalDate.parse("2023-06-28"));
        film.setDuration(-120);

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }
}