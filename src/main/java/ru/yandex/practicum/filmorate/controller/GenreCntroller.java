package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/genres")
public class GenreCntroller {
    private final FilmService service;

    public GenreCntroller(FilmService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Genre> getGenresList() {
        return service.getGenreList();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") int genreId) {
        return service.getGenreById(genreId);
    }
}
