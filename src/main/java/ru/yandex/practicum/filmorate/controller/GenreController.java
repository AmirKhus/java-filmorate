package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    private final GenreStorage genreStorage;

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable long id) {
        return genreStorage.getGenreById(id);
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenre();
    }
}
