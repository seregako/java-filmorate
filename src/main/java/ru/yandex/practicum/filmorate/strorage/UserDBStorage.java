package ru.yandex.practicum.filmorate.strorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.strorage.interfaces.FriendsStorage;
import ru.yandex.practicum.filmorate.strorage.interfaces.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDBStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserDBStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final FriendsStorage friendsStorage;

    public UserDBStorage(JdbcTemplate jdbcTemplate, FriendsStorage friendsStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendsStorage = friendsStorage;
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM user_table; DELETE FROM friends;ALTER TABLE user_table ALTER COLUMN id RESTART WITH 1 ";
        jdbcTemplate.execute(query);
        log.info("хранилище очищено");
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT  * FROM user_table";
        return jdbcTemplate.query(query, new UserMapper());
    }

    public Map<Integer, User> getUsers() {
        Map<Integer, User> usersMap = new HashMap<>();
        for (User user : findAll()) {
            usersMap.put(user.getId(), user);
        }
        return usersMap;
    }

    public Optional<User> findById(int userId) {
        String query = "SELECT *  FROM user_table  WHERE id = ?";
        return jdbcTemplate.query(query, new Object[]{userId}, new UserMapper()).stream().findAny();
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO user_table (name, email, login, birthday) VALUES (?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int newId = keyHolder.getKey().intValue();
        user.setId(newId);
        log.info("From add User: New User Created: {}", user);
        return user;
    }

    public User update(User user) {
        log.info("From update user: User for update: {}", user.toString());
        String query = "UPDATE user_table SET name = ?, email = ?, login = ?," +
                "birthday =? WHERE id = " + user.getId();
        jdbcTemplate.update(query, user.getName(), user.getEmail(),
                user.getLogin(), user.getBirthday());
        return user;
    }

    @Override
    public boolean exist(int userId) {
        String query = "SELECT COUNT (*) FROM user_table WHERE id = ?";
        Integer answer = jdbcTemplate.queryForObject(
                query, Integer.class, userId);
        return !answer.equals(0);
    }
}
