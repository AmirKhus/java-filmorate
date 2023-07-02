package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;

    public class FilmControllerTest {

        @Test
        void testAddFilmValidName() throws ValidationException {
            FilmController filmController = new FilmController();
            Film film = new Film();
            film.setName("Test Film");
            film.setDescription("This is a test film");
            film.setReleaseDate(Date.valueOf("2023-06-28"));
            film.setDuration(120);

            Assertions.assertDoesNotThrow(() -> filmController.addFilm(film));
        }

        @Test
        void testAddFilmEmptyName() {
            FilmController filmController = new FilmController();
            Film film = new Film();
            film.setName("");
            film.setDescription("This is a test film");
            film.setReleaseDate(Date.valueOf("2023-06-28"));
            film.setDuration(120);

            ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
            Assertions.assertEquals("Название фильма не может быть пустым!", exception.getMessage());
        }

        @Test
        void testAddFilmExceededDescriptionLength() {
            FilmController filmController = new FilmController();
            Film film = new Film();
            film.setName("Тестовый фильм");
            film.setDescription("Это очень длинное описание, которое превышает максимально допустимую длину в 200 символов. Это всего лишь тест. Это очень длинное описание, которое превышает максимально допустимую длину в 200 символов. Это всего лишь тест.");
            film.setReleaseDate(Date.valueOf("2023-06-28"));
            film.setDuration(120);

            ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
            Assertions.assertEquals("Превышена максимальная длина описания (200 символов)", exception.getMessage());
        }

        @Test
        void testAddFilmInvalidReleaseDate() {
            FilmController filmController = new FilmController();
            Film film = new Film();
            film.setName("Test Film");
            film.setDescription("This is a test film");
            film.setReleaseDate(Date.valueOf("1895-12-27"));
            film.setDuration(120);

            ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
            Assertions.assertEquals("Нельзя добавлять даты ниже 28 декабря 1895 года", exception.getMessage());
        }

        @Test
        void testAddFilmNegativeDuration() {
            FilmController filmController = new FilmController();
            Film film = new Film();
            film.setName("Test Film");
            film.setDescription("This is a test film");
            film.setReleaseDate(Date.valueOf("2023-06-28"));
            film.setDuration(-120);

            ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
            Assertions.assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
        }
    }