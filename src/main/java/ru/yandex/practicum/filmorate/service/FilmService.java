package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.strorage.interfaces.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;
    GenreStorage genreStorage;
    MpaStorage mpaStorage;
    private final LikesStorage likesStorage;

    public FilmService(@Qualifier("filmDBStorage") FilmStorage filmStorage, @Qualifier("userDBStorage") UserStorage userStorage,
                       GenreStorage genreStorage, MpaStorage mpaStorage, LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likesStorage = likesStorage;
    }

    public Film create(Film film) {
        if (!dateValidator(film)) throw new IllegalArgumentException("Слишком ранняя дата");
        else {
            return filmStorage.add(film);
        }
    }

    public Film getById(int filmId) {
        Film film = filmStorage.find(filmId);
        //film.setGenres(genreStorage.findByFilmId(filmId));
        film.setGenres(genreStorage.findByFilmId(filmId));
        return film;
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);

    }

    public void removeLike(int filmId, int userId) {
        if (!userStorage.exist(userId)) throw new NoIdException("no user with id " + userId);
        likesStorage.removeLike(filmId, userId);

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

    public Film update(Film film) {
        if (!dateValidator(film)) throw new IllegalArgumentException("Слишком ранняя дата");
        else {
            return filmStorage.update(film);
        }
    }

    public List<Film> getAll() {



        return filmStorage.findAll();

    }

    public void clearStorage() {
        filmStorage.clearStorage();
    }

    private boolean dateValidator(Film film) {
        return (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)));
    }
}
