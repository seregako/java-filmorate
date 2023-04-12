package ru.yandex.practicum.filmorate.strorage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private int id;
    private Map<Integer, Film> films = new HashMap<>();

    private Set<Film> popularFilms = new TreeSet<>(Comparator.comparing(Film::getRate).thenComparing(Film::getId)
            .reversed());


    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {throw new IllegalArgumentException("There is no film with id " + film.getId());}
            Film removedFilm = films.get(film.getId());
            popularFilms.remove(removedFilm);
            popularFilms.add(film);
            films.put(film.getId(), film);
   return film;
    }


    public Set<Film> findPopular() {
        return popularFilms;
    }

    public void clearStorage() {
        films.clear();
    }

    public Film find(int filmId) {
        return films.get(filmId);
    }

    public List<Film> findAll() {
        return films.entrySet()
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    public Film add(Film film) {
        id++;
        film.setId(id);
        films.put(film.getId(), film);
        popularFilms.add(film);
        return film;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = find(filmId);
        film.getLikes().add(userId);
        update(film);
    }

    public boolean exist (int filmId){
        return films.containsKey(filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        Film film = find(filmId);
        film.getLikes().remove(userId);
        update(film);
    }

    @Override
    public List<Mpa> allMpa() {
        return null;
    }

    @Override
    public Mpa MpaById(int mpaId) {
        return null;
    }

    @Override
    public Optional <Genre> GenreById(int genreId) {
        return null;
    }

    @Override
    public List<Genre> allGenres() {
        return null;
    }

    @Override
    public Set<Genre> getGenresByFilmId(int filmId) {
        return null;
    }
}
