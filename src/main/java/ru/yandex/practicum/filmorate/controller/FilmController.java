package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.store.FilmStore;

import java.util.*;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/films")

public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStore store;
    private int id = 0;
    public FilmController(FilmStore store) {
        this.store = store;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return store.giveAllFilms();
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable("id") int filmId) {
        return store.giveFilm(filmId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        id++;
        film.setId(id);
        store.addFilm(film);
        log.info("storage now: {} " + store.giveAllFilms());
        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        store.updateFilm(film);
        return film;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FilmStore getStore() {
        return store;
    }
}