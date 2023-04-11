package ru.yandex.practicum.filmorate.strorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmMapper;
import ru.yandex.practicum.filmorate.dao.FriendshipMapper;
import ru.yandex.practicum.filmorate.dao.UserMapper;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDBStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(FilmDao.class);

    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT  * from user_table";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    public Map<Integer, User> getUsers() {
        Map <Integer, User> usersMap = new HashMap<>();
        for (User user: findAll()){
            usersMap.put(user.getId(), user);
        }
        return usersMap;
    }

    public Optional<User> findById(int userId) {
        String sql = "SELECT *  FROM user_table  WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new UserMapper()).stream().findAny();
    }

    @Override
    public User add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into user_table (name, email, login, birthday) values(?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int newId = keyHolder.getKey().intValue();
        user.setId(newId);
        log.info("New User Created: {}", user);
        return user;
    }

    public User update(User user) {
        log.info("Юзер для обновления" + user.toString());
        String sql = "update user_table set name = ?, email = ?, login = ?," +
                "birthday =? where id = "+ user.getId();
        jdbcTemplate.update(sql, user.getName(), user.getEmail(),
                user.getLogin(), user.getBirthday());
        return user;
    }

    @Override
    public boolean exist(int userId) {
        log.info("Список юзеров"+ getUsers());
        return getUsers().containsKey(userId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?,?)";
        jdbcTemplate.update(sql, userId,friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sql  = "DELETE FROM friends WHERE user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId,friendId);
    }

    @Override
    public List <Friendship> findFriends(int userId) {
       String sql = "SELECT * FROM friends WHERE user_id ="+ userId;
        return jdbcTemplate.query(sql, new FriendshipMapper());
    }


}
