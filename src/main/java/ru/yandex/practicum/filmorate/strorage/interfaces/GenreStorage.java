package ru.yandex.practicum.filmorate.strorage.interfaces;

import ru.yandex.practicum.filmorate.model.CombinedGenre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    List<CombinedGenre> allTable();

    List<Genre> findByFilmId(int genreId);

    List<Genre> findAll();

    void addGenres(Film film);

    Optional<Genre> findById(int genreId);

    void removeByFilmId(int filmId);

}
