package ru.yandex.practicum.filmorate.store;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FilmStore {
    private Map<Integer, Film> films = new HashMap<>();

    public void putFilm(Film film) {
        films.put(film.getId(), film);
    }

    public void deletAllFilms() {
        films.clear();
    }

    public Film getFilm(int filmId) {
        return films.get(filmId);
    }

    public Map<Integer, Film> getAllFilms() {
        return films;
    }

    public void postFilm(Film film) {
        films.put(film.getId(), film);
    }


}
