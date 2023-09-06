package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private final FilmStorage filmStorage;
    @Autowired
    private final FilmService filmService;


    @PostMapping
    public @Valid Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public ResponseEntity<Object> updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }


    @GetMapping("/{id}")
    public Film getFilm(@PathVariable String id) {
        return filmStorage.getFilmById(Long.parseLong(id));
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/popular")
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getMostPopularFilm(Integer.parseInt(count));
    }

    @PutMapping("/{id}/like/{filmId}")
    public void like(@PathVariable String id, @PathVariable String filmId) {
        filmService.addLike(Long.parseLong(id), Long.parseLong(filmId));
    }

    @DeleteMapping("/{id}/like/{filmId}")
    public void deleteLike(@PathVariable String id, @PathVariable String filmId) {
        filmService.deleteLike(Long.parseLong(id), Long.parseLong(filmId));
    }

}