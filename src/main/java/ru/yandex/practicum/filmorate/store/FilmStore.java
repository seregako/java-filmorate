package ru.yandex.practicum.filmorate.store;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class FilmStore {
    private Map<Integer, Film> films = new HashMap<>();

    public void updateFilm(Film film) {
        if (films.containsKey(film.getId())){
        films.put(film.getId(), film);}
        else throw new IllegalArgumentException("There is no film with id " + film.getId());
    }

    public void clearFilmsStorage() {
        films.clear();
    }

    public Film giveFilm(int filmId) {
        return films.get(filmId);
    }

    public List< Film> giveAllFilms() {
        return films.entrySet()
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }


}
