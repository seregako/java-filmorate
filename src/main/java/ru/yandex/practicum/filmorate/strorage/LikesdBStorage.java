package ru.yandex.practicum.filmorate.strorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.strorage.interfaces.LikesStorage;

import java.util.HashSet;
import java.util.Set;

@Repository
public class LikesdBStorage implements LikesStorage {
   private final JdbcTemplate jdbcTemplate;

    public LikesdBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String query = "DELETE FROM likes WHERE (film_id = ? and user_id  = ?)";
        jdbcTemplate.update(query, filmId, userId);
    }

   @Override
    public Set<Integer> getLikesById(int filmId) {
        String query = "SELECT fl.user_id FROM likes AS fl WHERE fl.film_id = ?";
        return new HashSet<>(jdbcTemplate.queryForList(query, Integer.class, filmId));
    }
}
