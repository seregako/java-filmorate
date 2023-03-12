package ru.yandex.practicum.filmorate.strorage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {

  void updateFilm(Film film);

    void clearFilmsStorage();

    Film giveFilm(int filmId);

    List< Film> giveAllFilms();

    void addFilm(Film film);

}
