package ru.yandex.practicum.filmorate.strorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmMapper;
import ru.yandex.practicum.filmorate.dao.GenreMapper;
import ru.yandex.practicum.filmorate.dao.MpaMapper;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Repository
public class FilmDBStorage implements FilmStorage {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    public FilmDBStorage(@Qualifier("userDBStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public Film find(int id) {
        log.info("актуальное хранилище фильмов: "+findAll());
        if (!exist(id)) throw new NoIdException("Wrong film id!");
        String sql = "SELECT *, m.name AS mpa_name FROM film AS f LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id WHERE f.id = ?";
        Film film = jdbcTemplate.query(sql, new Object[]{id}, new FilmMapper()).stream().findAny().get();
        film.setGenres(getGenresByFilmId(id));
        film.setRating(getLikesById(film.getId()).size());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!exist(film.getId())) throw new NoIdException("Wrong film Id");
        String clearGenreSql = "delete from films_genres where film_id =  " + film.getId();
        jdbcTemplate.execute(clearGenreSql);
        log.info("Фильм для обновления" + film);
        addGenres(film);
        String sql = "update film set name = ?, description = ?, duration = ?," +
                " release_date =?, mpa_id = ? where id = " + film.getId();
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getMpa().getId());
        film.setGenres(getGenresByFilmId(film.getId()));
        return film;
    }

    @Override
    public void clearStorage() {
        String sql = "delete from film; delete from likes; delete from films_genres";
        jdbcTemplate.execute(sql);
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT  * from film as f left join mpa as m where f.mpa_id = m.mpa_id";
        List<Film> films = jdbcTemplate.query(sql, new FilmMapper());
        for (Film film : films) {
            film.setGenres(getGenresByFilmId(film.getId()));
            film.setRating(getLikesById(film.getId()).size());
        }
        return films;
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into film(name, description, duration, release_date, mpa_id) values(?,?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
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
        addGenres(film);
        film.setGenres(getGenresByFilmId(newId));
        log.info("New Film Created: {}", film);
        return film;
    }

    @Override
    public Set<Film> findPopular() {
        Set<Film> popularFilms = new TreeSet<>(Comparator.comparing(Film::getRating).thenComparing(Film::getId)
                .reversed());
        List<Film> films = findAll();
        for (Film film : films) {
            film.setRating(getLikesById(film.getId()).size());
            popularFilms.add(film);
        }
        return popularFilms;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public void addLike(int filmId, int userId) {
        if (!userStorage.exist(userId)) throw new NoIdException("addLike no user with id " + userId);
        if (!exist(filmId))throw new NoIdException("addLike no film with id " + filmId);
        String sql = "insert into likes (film_id, user_id) values (?,?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public boolean exist(int id) {
        String sql = "select count(*) from FILM where id = ?";
        Integer answer = (Integer) jdbcTemplate.queryForObject(
                sql, Integer.class, id);
        return !answer.equals(0);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        if (!userStorage.exist(userId)) throw new NoIdException("no user with id " + userId);
        String sql = "delete from likes where (film_id = ? and user_id  = ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Mpa> allMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, new MpaMapper());
    }

    @Override
    public Mpa MpaById(int mpaId) {
        if (!mpaExist(mpaId)) throw new NoIdException("No MPA with id " + mpaId);
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.query(sql, new Object[]{mpaId}, new MpaMapper()).stream().findAny().get();
    }

    @Override
    public Optional<Genre> GenreById(int genreId) {
        if (!genreExist(genreId)) throw new NoIdException("No genre with id " + genreId);
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        log.info("возврвщаемый жанр " + jdbcTemplate.query(sql, new Object[]{genreId}, new GenreMapper()).stream().findAny());
        return jdbcTemplate.query(sql, new Object[]{genreId}, new GenreMapper()).stream().findAny();
    }

    @Override
    public List<Genre> allGenres() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    private boolean genreExist(int id) {
        String sql = "select count(*) from genre where genre_id = ?";
        Integer answer = (Integer) jdbcTemplate.queryForObject(
                sql, Integer.class, id);
        return !answer.equals(0);
    }

    private boolean mpaExist(int id) {
        String sql = "select count(*) from mpa where mpa_id = ?";
        Integer answer = (Integer) jdbcTemplate.queryForObject(
                sql, Integer.class, id);
        return !answer.equals(0);
    }

    private Set<Integer> getLikesById(int filmId) {
        String sql = "SELECT fl.user_id FROM likes AS fl WHERE fl.film_id = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, filmId));
    }

    public Set<Genre> getGenresByFilmId(int filmId) {
        String sql = "select g.genre_id, g.name from genre as g right join films_genres as fg on fg.genre_id = g.genre_id where fg.film_id =  " + filmId;
        Set<Genre> filmGenres = new TreeSet<>(Comparator.comparing(Genre::getId).thenComparing(Genre::getName));
        filmGenres.addAll(jdbcTemplate.query(sql, new GenreMapper()));
        return filmGenres;
    }

    private void addGenres(Film film) {
        Set<Genre> genres = film.getGenres();
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                writeGenreToTable(film.getId(), genre.getId());
            }
        }
    }

    private void writeGenreToTable(int filmId, int genreId) {
        String sql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, genreId);
    }
}
