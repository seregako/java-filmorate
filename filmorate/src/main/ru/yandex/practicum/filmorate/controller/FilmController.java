package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.store.FilmStore;

import javax.validation.Valid;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;
//import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    FilmStore store;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    int id = 0;

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> list = store.getAllFilms().entrySet()
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
        return list;
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable("id") int getId) {
        return store.getFilm(getId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        id++;
        film.setId(id);
        store.postFilm(film);
        log.info("storage now " + store.getAllFilms());
        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        if (store.getAllFilms().containsKey(film.getId())) {
            store.putFilm(film);
            return film;
        } else throw new IllegalArgumentException("There is no film with id " + film.getId());
    }

    public void setId(int id) {
        this.id = id;
    }

    public FilmStore getStore() {
        return store;
    }
}