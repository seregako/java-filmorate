package ru.yandex.practicum.filmorate.strorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.strorage.interfaces.MpaStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDBStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> findById(int mpaId) {
        String query = "SELECT * FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.query(query, new Object[]{mpaId}, new MpaMapper())
                .stream().findAny();
    }

    @Override
    public List<Mpa> findAll() {
        String query = "SELECT * FROM mpa";
        return jdbcTemplate.query(query, new MpaMapper());
    }
}
