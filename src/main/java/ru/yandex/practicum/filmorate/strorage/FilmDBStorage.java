package ru.yandex.practicum.filmorate.strorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Slf4j
@Repository
public class FilmDBStorage implements FilmStorage {
    FilmDao filmDao;

    public FilmDBStorage(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    @Override
    public Optional<Film> find(int id) {
        return filmDao.find(id);
    }

    @Override
    public Film update(Film film) {
return filmDao.update(film);
    }

    @Override
    public void clearStorage() {

    }

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Film add(Film film) {
        return filmDao.create(film);
    }

    @Override
    public Set<Film> findPopular() {
        return  filmDao.findPopular();
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public void addLike(int filmId, int userId) {
        filmDao.addLike(filmId,userId);
    }

    @Override
    public boolean exist(int filmId) {
        return false;
    }

    @Override
    public void removeLike(int filmId, int userId) {
        filmDao.removeLike (filmId, userId);
    }

    @Override
    public List<Mpa> allMpa() {
        return filmDao.allMpa();
    }

    @Override
    public Mpa MpaById(int mpaId) {
        return filmDao.MpaById(mpaId);
    }

    @Override
    public Genre GenreById(int genreId) {
        return filmDao.GenreById(genreId);
    }

    @Override
    public List<Genre> allGenres() {
        return filmDao.allGenres();
    }

}
