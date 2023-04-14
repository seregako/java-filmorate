package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/films")

public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    public static final int POPULAR_LIST_SIZE = 10;
    private final FilmService service;

    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAll();
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable("id") int filmId) {
        return service.getById(filmId);
    }

    @PostMapping
    public Film post(@Valid @RequestBody Film film) {
        film = service.create(film);
        log.info("storage now: {} ", service.getAll());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        film = service.update(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        service.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        service.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopulareFilms(@RequestParam(required = false) Integer count) {
        if (count == null) {
            return service.getPopular(POPULAR_LIST_SIZE);
        }
        return service.getPopular(count);
    }

}