package ru.yandex.practicum.filmorate.strorage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film find(int id);

    Film update(Film film);

    void clearStorage();

    List<Film> findAll();

    Film add(Film film);

    Set<Film> findPopular();

    void setId(int id);

    void addLike(int filmId, int userId);

    boolean exist(int filmId);
}
