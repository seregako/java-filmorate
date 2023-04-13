package ru.yandex.practicum.filmorate.strorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreMapper;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.strorage.interfaces.GenreStorage;

import java.util.*;

@Repository
public class GenreDBStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre findById(int genreId) {
            if (!genreExist(genreId)) throw new NoIdException("No genre with id " + genreId);
            String sql = "SELECT * FROM genre WHERE genre_id = ?";
            return jdbcTemplate.query(sql, new Object[]{genreId}, new GenreMapper()).stream().findAny().get();
        }


    @Override
    public Set<Genre> findByFilmId(int filmId) {
        String query = "SELECT g.genre_id, g.name FROM genre as g RIGHT JOIN films_genres AS fg ON fg.genre_id = " +
                "g.genre_id WHERE fg.film_id =  " + filmId;
        Set<Genre> filmGenres = new TreeSet<>(Comparator.comparing(Genre::getId).thenComparing(Genre::getName));
        filmGenres.addAll(jdbcTemplate.query(query, new GenreMapper()));
        return filmGenres;
    }

    @Override
    public List<Genre> findAll() {
        String query = "SELECT * FROM genre";
        return jdbcTemplate.query(query, new GenreMapper());
    }

    @Override
    public void addGenres(Film film) {
        Set<Genre> genres = film.getGenres();
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                writeGenreToTable(film.getId(), genre.getId());
            }
        }
    }

    private void writeGenreToTable(int filmId, int genreId) {
        String query = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(query, filmId, genreId);
    }

    private boolean genreExist(int id) {
        String query = "SELECT COUNT(*) FROM genre WHERE genre_id = ?";
        Integer answer = jdbcTemplate.queryForObject(
                query, Integer.class, id);
        return !answer.equals(0);
    }
}
