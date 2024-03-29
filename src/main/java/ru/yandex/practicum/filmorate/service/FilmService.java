package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.strorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.strorage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FilmService {
    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getById(int filmId) {
        return filmStorage.find(filmId).orElseThrow(() -> new NoIdException("Wrong film Id"));
    }

    public void addLike(int filmId, int userId) {
        if (!userStorage.exist(userId)) { throw new NoIdException("No user with id " + userId);}
            Film film = filmStorage.find(filmId).orElseThrow(() -> new NoIdException("Wrong film Id"));
            film.getLikes().add(userId);
            filmStorage.update(film);
    }

    public void removeLike(int filmId, int userId) {
        if (!userStorage.exist(userId)) {throw new NoIdException("No user with id " + userId);}
            Film film = filmStorage.find(filmId).orElseThrow(() -> new NoIdException("Wrong film Id"));
            film.getLikes().remove(userId);
            filmStorage.update(film);
    }

    public List<Film> getPopular(int count) {
        List<Film> popularsList = new ArrayList<>(filmStorage.findPopular());
        int actualCount;
        if (popularsList.size() >= count) {
            actualCount = count;
        } else {
            actualCount = popularsList.size();
        }
        List<Film> populars = new LinkedList<>();
        for (int i = 0; i < actualCount; i++) {
            populars.add(popularsList.get(i));
        }
        return populars;
    }

    public void post(Film film) {
        filmStorage.add(film);
    }

    public void put(Film film) {
        if (!filmStorage.exist(film.getId())) {throw new NoIdException("Wrong film Id");}
            filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.findAll();
    }
}
