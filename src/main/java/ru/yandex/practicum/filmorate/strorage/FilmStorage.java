package ru.yandex.practicum.filmorate.strorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {

    void update(Film film);

    void clearStorage();

    List<Film> findAll();

    void add(Film film);

    Set<Film> findPopular();

    void setId(int id);

    boolean exist(int filmId);
}
