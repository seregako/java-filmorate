package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.CombinedGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CombinedGenreMapper implements RowMapper <CombinedGenre> {
        @Override
        public CombinedGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
            CombinedGenre combinedGenre = new CombinedGenre();
            combinedGenre.setFilmId(rs.getInt("film_id"));
            combinedGenre.setGenreId(rs.getInt("genre_id"));
            combinedGenre.setGenreName(rs.getString("genre_name"));
            return combinedGenre;
        }
}
