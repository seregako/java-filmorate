package ru.yandex.practicum.filmorate.strorage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private int id;
    private Map<Integer, Film> films = new HashMap<>();

    private Set<Film> popularFilms = new TreeSet<>(Comparator.comparing(Film::getRate).thenComparing(Film::getId)
            .reversed());

    public void updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            Film removedFilm = films.get(film.getId());
            popularFilms.remove(removedFilm);
            popularFilms.add(film);
            films.put(film.getId(), film);
        } else throw new IllegalArgumentException("There is no film with id " + film.getId());
    }


    public Set<Film> getPopularFilms() {
        return popularFilms;
    }

    public void clearFilmsStorage() {
        films.clear();
    }

    public Film giveFilm(int filmId) {
        return films.get(filmId);
    }

    public List<Film> giveAllFilms() {
        return films.entrySet()
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    public void addFilm(Film film) {
        id++;
        film.setId(id);
        films.put(film.getId(), film);
        popularFilms.add(film);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }
}
