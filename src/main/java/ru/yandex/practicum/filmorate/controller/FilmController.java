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
    private final int  POPULAR_FILMSLIST_DEFAULT_SIZE = 10;
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
    public Film createFilm(@Valid @RequestBody Film film) {
        service.post(film);
        log.info("storage now: {} " , service.getAll() + "\n List of popularity", service.getPopular(10));
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        service.put(film);
        log.info("storage now: {} " , service.getAll() + "\n List of popularity", service.getPopular(10));
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        service.addLike(filmId, userId);
        log.info("storage after like: {} ", service.getAll() + "\n List of popularity", service.getPopular(10));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable int userId) {
            service.removeLike(filmId, userId);
            log.info("storage after dislike: {} ", service.getAll() + "\n List of popularity", service.getPopular(10));
    }

    @GetMapping("/popular")
    public List<Film> getPopulareFilms(@RequestParam(required = false) Integer count) {
        if (count == null) {
            return service.getPopular( POPULAR_FILMSLIST_DEFAULT_SIZE);
        }
        return service.getPopular(count);
    }
}