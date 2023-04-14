package ru.yandex.practicum.filmorate.strorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.strorage.interfaces.MpaStorage;

import java.util.List;

@Repository
public class MpaDBStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa findById(int mpaId) {
        if (!mpaExist(mpaId)) throw new NoIdException("No MPA with id " + mpaId);
        String query = "SELECT * FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.query(query, new Object[]{mpaId}, new MpaMapper()).stream().findAny().get();
    }

    @Override
    public List<Mpa> findAll() {
        String query = "SELECT * FROM mpa";
        return jdbcTemplate.query(query, new MpaMapper());
    }

    private boolean mpaExist(int id) {
        String query = "SELECT COUNT(*) FROM mpa WHERE mpa_id = ?";
        Integer answer = (Integer) jdbcTemplate.queryForObject(
                query, Integer.class, id);
        return !answer.equals(0);
    }
}
