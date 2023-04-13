package ru.yandex.practicum.filmorate.strorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendshipMapper;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.strorage.interfaces.FriendsStorage;

import java.util.List;

@Repository
public class FriendsDBStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String query = "INSERT INTO friends (user_id, friend_id) VALUES (?,?)";
        jdbcTemplate.update(query, userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String query = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(query, userId, friendId);
    }

    @Override
    public List<Friendship> findFriends(int userId) {
        String query = "SELECT * FROM friends WHERE user_id =" + userId;
        return jdbcTemplate.query(query, new FriendshipMapper());
    }
}
