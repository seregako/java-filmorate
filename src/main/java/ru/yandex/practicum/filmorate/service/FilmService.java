package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.model.CombinedGenre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.strorage.interfaces.*;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikesStorage likesStorage;

    public FilmService(@Qualifier("filmDBStorage") FilmStorage filmStorage, @Qualifier("userDBStorage") UserStorage userStorage,
                       GenreStorage genreStorage, MpaStorage mpaStorage, LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likesStorage = likesStorage;
    }

    public Film create(Film film) {
        if (!dateValidator(film)) throw new IllegalArgumentException("Слишком ранняя дата");
        else {
            return filmStorage.add(film);
        }
    }

    public Film getById(int filmId) {
        Film film = filmStorage.find(filmId);
        film.setGenres(genreStorage.findByFilmId(filmId));
        return film;
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);

    }

    public void removeLike(int filmId, int userId) {
        if (!userStorage.exist(userId)) throw new NoFoundException("no user with id " + userId);
        likesStorage.removeLike(filmId, userId);

    }

    public List<Film> getPopular(int count) {
        List<Film> popularsList = new ArrayList<>(filmStorage.findPopular());
        int actualCount;
        if (popularsList.size() >= count) {
            actualCount = count;
        } else {
            actualCount = popularsList.size();
        }
        List<Film> populars = new LinkedList<>();
        for (int i = 0; i < actualCount; i++) {
            populars.add(popularsList.get(i));
        }
        return populars;
    }

    public Film update(Film film) {
        if (!dateValidator(film)) throw new BadRequestException("Слишком ранняя дата");
        else {
            return filmStorage.update(film);
        }
    }

    public List<Film> getAll() {
        List<Film> films = filmStorage.findAll();
        List<CombinedGenre> allFilmsGenres = genreStorage.allTable();
        Set<Integer> filmsIds = new LinkedHashSet<>();
        for (Film film : films) {
            filmsIds.add(film.getId());
        }
        Map<Integer, List<Genre>> genresByFilms = new HashMap<>();
        for (Integer filmId : filmsIds) {
            List<Genre> genres = new ArrayList<>();
            for (CombinedGenre cGenre : allFilmsGenres) {
                if (filmId.equals(cGenre.getFilmId())) {
                    genres.add(new Genre(cGenre.getGenreId(), cGenre.getGenreName()));
                }
            }
            genresByFilms.put(filmId, genres);
        }
        List<Film> filmsWithGenres = new ArrayList<>();
        for (Film film : films) {
            if (genresByFilms.containsKey(film.getId())) {
                film.setGenres(genresByFilms.get(film.getId()));
            } else {
                film.setGenres(new ArrayList<>());
            }
            film.setRating(setRateFromStorage(film.getId()));
            filmsWithGenres.add(film);
        }
        return filmsWithGenres;
    }

    public void clearStorage() {
        filmStorage.clearStorage();
    }

    private int setRateFromStorage(int filmId) {
        int countLikes = 0;
        for (Like like : likesStorage.getAll()) {
            if (like.getFilmId() == filmId) {
                countLikes++;
            }
        }
        return countLikes;
    }


    private boolean dateValidator(Film film) {
        return (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)));
    }
}
