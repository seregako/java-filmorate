package ru.yandex.practicum.filmorate.strorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.strorage.interfaces.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Repository
public class FilmDBStorage implements FilmStorage {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikesStorage likesStorage;

    public FilmDBStorage(UserStorage userStorage, JdbcTemplate jdbcTemplate, GenreStorage genreStorage,
                         MpaStorage mpaStorage, LikesStorage likesStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likesStorage = likesStorage;
    }

    @Override
    public Film find(int id) {
        if (!exist(id)) throw new NoFoundException("Wrong film id!");
        String query = "SELECT *, m.name AS mpa_name FROM film AS f LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id WHERE f.id = ?";
        Film film = jdbcTemplate.query(query, new Object[]{id}, new FilmMapper()).stream().findAny().get();
        film.setGenres(genreStorage.findByFilmId(id));
        film.setRating(likesStorage.getLikesById(film.getId()).size());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!exist(film.getId())) throw new NoFoundException("Wrong film Id");
        log.info("Фильм для обновления" + film);
        genreStorage.removeByFilmId(film.getId());
        genreStorage.addGenres(film);
        String query = "UPDATE film SET name = ?, description = ?, duration = ?," +
                " release_date =?, mpa_id = ? where id = " + film.getId();
        jdbcTemplate.update(query, film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getMpa().getId());
        film.setGenres(genreStorage.findByFilmId(film.getId()));
        return film;
    }

    @Override
    public void clearStorage() {
        String query = "DELETE FROM film; DELETE FROM films_genres; ALTER TABLE film ALTER COLUMN id RESTART WITH 1 ";
        jdbcTemplate.execute(query);
    }

    @Override
    public List<Film> findAll() {
        String query = "SELECT  *, m.name as mpa_name FROM film as f LEFT JOIN mpa as m WHERE f.mpa_id = m.mpa_id";
        List<Film> films = jdbcTemplate.query(query, new FilmMapper());
        return films;
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO film(name, description, duration, release_date, mpa_id) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int newId = keyHolder.getKey().intValue();
        log.info("created film id: {}", newId);
        film.setId(newId);
        genreStorage.addGenres(film);
        film.setGenres(genreStorage.findByFilmId(newId));
        film.setMpa(mpaStorage.findById(film.getMpa().getId()).orElse(new Mpa()));
        log.info("New Film Created: {}", film);
        return film;
    }

    @Override
    public Set<Film> findPopular() {
        Set<Film> popularFilms = new TreeSet<>(Comparator.comparing(Film::getRating).thenComparing(Film::getId)
                .reversed());
        List<Film> films = findAll();
        for (Film film : films) {
            film.setRating(likesStorage.getLikesById(film.getId()).size());
            popularFilms.add(film);
        }
        return popularFilms;
    }

    @Override
    public void setId(int id) {
    }

    @Override
    public void addLike(int filmId, int userId) {
        if (!userStorage.exist(userId)) throw new NoFoundException("addLike no user with id " + userId);
        if (!exist(filmId)) throw new NoFoundException("addLike: no film with id " + filmId);
        String query = "INSERT INTO likes (film_id, user_id) VALUES (?,?)";
        jdbcTemplate.update(query, filmId, userId);
    }

    @Override
    public boolean exist(int id) {
        String query = "SELECT COUNT (*) FROM FILM WHERE id = ?";
        Integer answer = jdbcTemplate.queryForObject(query, Integer.class, id);
        return !answer.equals(0);
    }

}
