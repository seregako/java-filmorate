package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.strorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.strorage.InMemoryUserStorage;

import java.util.*;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/films")

public class FilmController {
    private final int FILMSLIST_DEFAULT_SIZE = 10;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final InMemoryFilmStorage filmStore;
    private final InMemoryUserStorage userStore;
    private final FilmService service;

    //private int id = 0;
    public FilmController(InMemoryFilmStorage filmStore, InMemoryUserStorage userStore, FilmService service) {
        this.filmStore = filmStore;
        this.userStore = userStore;
        this.service = service;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStore.giveAllFilms();
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable("id") int filmId) {
        if (!filmStore.getFilms().containsKey(filmId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there is no film with id " + filmId);
        }
        return filmStore.giveFilm(filmId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        filmStore.addFilm(film);
        log.info("storage now: {} " + filmStore.giveAllFilms() + "\n List of popularity" + filmStore.getPopularFilms());
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        filmStore.updateFilm(film);
        log.info("storage now: {} " + filmStore.giveAllFilms() + "\n List of popularity" + filmStore.getPopularFilms());
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        if (!filmStore.getFilms().containsKey(filmId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there is no film with id " + filmId);
        }
        if (!userStore.getUsers().containsKey(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there is no user with id " + userId);
        }
        service.addLike(filmId, userId);
        log.info("storage after like: {} " + filmStore.giveAllFilms() + "\n List of popularity" + filmStore.getPopularFilms());
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        if (!filmStore.getFilms().containsKey(filmId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there is no film with id " + filmId);
        } else if (!userStore.getUsers().containsKey(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there is no user with id " + userId);
        } else {
            service.removeLike(filmId, userId);
            log.info("storage after dislike: {} " + filmStore.giveAllFilms() + "\n List of popularity" + filmStore.getPopularFilms());
        }
    }

    @GetMapping("/popular")
    public List<Film> getPopulareFilms(@RequestParam(required = false) Integer count) {
        if (count == null) {
            return service.givePopulareFilms(FILMSLIST_DEFAULT_SIZE);
        }
        return service.givePopulareFilms(count);
    }

    public InMemoryFilmStorage getStore() {
        return filmStore;
    }
}