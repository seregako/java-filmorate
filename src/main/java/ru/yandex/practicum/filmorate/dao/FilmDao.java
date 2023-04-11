package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.Date;
import java.util.*;

@Component
public class FilmDao {
    private Set<Film> popularFilms = new TreeSet<>(Comparator.comparing(Film::getRating).thenComparing(Film::getId)
            .reversed());

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(FilmDao.class);

    public FilmDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> findAll() {
        String sql = "SELECT  * from film as f left join mpa as m where f.mpa_id = m.mpa_id";
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    public Optional<Film> find(int id) {
        String sql = "SELECT *, m.name AS mpa_name FROM film AS f LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id WHERE f.id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new FilmMapper()).stream().findAny();
    }

    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
       // addGenres(film);
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
        film.setId(newId);
        //film.setGenres(getGenresByFilmId(film.getId()));
        log.info("New Film Created: {}", film.getName());
        return film;
    }

    public Film update(Film film) {
       // if (!exist(id)) throw new NoIdException("нет фильма с id " + id);
        String clearGenreSql = "delete from films_genres where film_id =  "+film.getId();
        jdbcTemplate.execute (clearGenreSql);
        log.info("Фильм для обновления" + film);
        addGenres(film);
        String sql = "update film set name = ?, description = ?, duration = ?," +
                " release_date =?, mpa_id = ? where id = "+ film.getId();
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getMpa().getId());
    return film;
    }

    private void writeGenreToTable (int filmId, int genreId){
        String sql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, genreId);
    }

   private void addGenres (Film film){
        Set <Genre> genres =  film.getGenres();
       if (!genres.isEmpty()) {
           for (Genre genre: genres ){
                writeGenreToTable(film.getId(), genre.getId());
            }
        }
    }

    private List <Genre> getGenresByFilmId (int filmId) {
        String sql = "SELECT g.id, g.name FROM genre AS g JOIN film_genres as fg ON g.genre_id = fg.genre_id WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, new GenreMapper());
    }


    public void addLike (int filmId, int userId){
        String sql = "insert into likes (film_id, user_id) values (?,?)";
    jdbcTemplate.update(sql, filmId, userId);
    }


    public void delete(int id) {
        String sql = "delete from film where id = ?";
        int delete = jdbcTemplate.update(sql, id);
        if (delete == 1) {
            log.info("Film Deleted: {}", id);
        }
    }

    public boolean exist(int id) {
        String sql = "select count(*) from FILM where id = ?";
        Integer answer = (Integer) jdbcTemplate.queryForObject(
                sql, Integer.class, id);
        return !answer.equals(0);
    }

    public void removeLike(int filmId, int userId) {
      String sql =  "delete from likes where (film_id = ? and user_id  = ?)";
      jdbcTemplate.update(sql, filmId,userId);
    }

        private Set<Integer> getLikesById(int filmId) {
            String sql = "SELECT fl.user_id FROM likes AS fl WHERE fl.film_id = ?";
            return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, filmId));
        }


    public Set<Film> findPopular() {
        List <Film> films = findAll();
        for (Film film: films){
           film.setRating(getLikesById(film.getId()).size());
           popularFilms.add(film);
        }
        return this.popularFilms;
    }

    public List<Mpa> allMpa() {
    String sql = "SELECT * FROM mpa";
    return jdbcTemplate.query(sql,new MpaMapper());
    }

    public Mpa MpaById(int mpaId) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.query(sql, new Object[]{mpaId},new MpaMapper()).stream().findAny().get();
    }

    public List <Genre> allGenres(){
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql,new GenreMapper());
    }

    public Genre GenreById(int genreId) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        return jdbcTemplate.query(sql, new Object[]{genreId},new GenreMapper()).stream().findAny().get();
    }
}
