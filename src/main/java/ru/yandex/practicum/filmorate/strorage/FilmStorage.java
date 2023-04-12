package ru.yandex.practicum.filmorate.strorage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {
    Film find(int id);

    Film update(Film film);

    void clearStorage();

    List<Film> findAll();

    Film add(Film film);

    Set<Film> findPopular();

    void setId(int id);

    public void addLike (int filmId, int userId);

    boolean exist(int filmId);

    void removeLike(int filmId, int userId);

    List<Mpa> allMpa();

    Mpa MpaById(int mpaId);

    Optional <Genre> GenreById(int genreId);

    List<Genre> allGenres();
}
