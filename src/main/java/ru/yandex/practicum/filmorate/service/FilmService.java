package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.strorage.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    public FilmService(@Qualifier("filmDBStorage") FilmStorage filmStorage, @Qualifier("userDBStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Mpa> getMpaList() {
        return filmStorage.allMpa();
    }

    public Mpa getMpaById(int mpaId) {
        return filmStorage.mpaById(mpaId);
    }

    public Film post(Film film) {
        if (!dateValidator(film)) throw new IllegalArgumentException("Слишком ранняя дата");
        else {
            return filmStorage.add(film);
        }
    }

    public Film getById(int filmId) {
        Film film = filmStorage.find(filmId);
        film.setGenres(filmStorage.getGenresByFilmId(filmId));
        return film;
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);

    }

    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);

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

    public Film put(Film film) {
        if (!dateValidator(film)) throw new IllegalArgumentException("Слишком ранняя дата");
        else {
            return filmStorage.update(film);
        }
    }

    public List<Film> getAll() {
        return filmStorage.findAll();
    }

    public Genre getGenreById(int genreId) {
        return filmStorage.genreById(genreId).orElseThrow(() -> new NoIdException("Wrong genre Id"));
    }

    public List<Genre> getGenreList() {
        return filmStorage.allGenres();
    }

    private boolean dateValidator(Film film) {
        return (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)));
    }

    public void clearStorage() {
        filmStorage.clearStorage();
    }
}
