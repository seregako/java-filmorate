package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.strorage.interfaces.GenreStorage;

import java.util.List;

@Validated
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreStorage storage;

    public GenreController(GenreStorage storage) {
        this.storage = storage;
    }

    @GetMapping()
    public List<Genre> getGenresList() {
        return storage.findAll();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") int genreId) {
        return storage.findById(genreId);
    }
}
