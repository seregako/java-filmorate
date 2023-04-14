package ru.yandex.practicum.filmorate.strorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.CombinedGenreMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.CombinedGenre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.strorage.interfaces.GenreStorage;

import java.util.*;

@Repository
@Slf4j
public class GenreDBStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Genre> findById(int genreId) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        return jdbcTemplate.query(sql, new Object[]{genreId}, new GenreMapper()).stream().findAny();
    }

    @Override
    public void removeByFilmId(int filmId) {
        String query = "DELETE FROM films_genres WHERE film_id = " + filmId;
        jdbcTemplate.execute(query);
    }


    @Override
    public List<CombinedGenre> allTable() {
        String query = "SELECT f.film_id, g.genre_id, g.name AS genre_name FROM films_genres AS f LEFT JOIN GENRE g " +
                "on f.GENRE_ID = g.GENRE_ID";
        return jdbcTemplate.query(query, new CombinedGenreMapper());
    }

    @Override
    public List<Genre> findByFilmId(int filmId) {
        String query = "SELECT g.genre_id, g.name FROM genre as g RIGHT JOIN films_genres AS fg ON fg.genre_id = " +
                "g.genre_id WHERE fg.film_id =  " + filmId;
        List<Genre> filmGenres = new ArrayList<>();
        filmGenres.addAll(jdbcTemplate.query(query, new GenreMapper()));
        return filmGenres;
    }

    @Override
    public List<Genre> findAll() {
        String query = "SELECT * FROM genre ORDER BY genre_id";
        return jdbcTemplate.query(query, new GenreMapper());
    }

    @Override
    public void addGenres(Film film) {
        List<Genre> genres = film.getGenres();
        log.info("films genres: {}", genres);
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                writeGenreToTable(film.getId(), genre.getId());
            }
        }
    }

    private void writeGenreToTable(int filmId, int genreId) {
        String query = "MERGE INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(query, filmId, genreId);
    }
}
