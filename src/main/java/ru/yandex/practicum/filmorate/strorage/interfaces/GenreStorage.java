package ru.yandex.practicum.filmorate.strorage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;
import java.util.Set;

public interface GenreStorage {

    Set<Genre> findByFilmId(int genreId);

    List<Genre> findAll();

    void addGenres(Film film);

    Genre findById(int genreId);
}
