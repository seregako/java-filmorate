package ru.yandex.practicum.filmorate.strorage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();

    private int id;

    public User add(User user) {
        id++;
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        users.put(id, user);
        return user;
    }

    public void deleteAll() {
        users.clear();
    }

    public Optional<User> findById(int userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public List<User> findAll() {
        return users.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public User update(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
        return user;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean exist(int userId) {
        return users.containsKey(userId);
    }

    @Override
    public void addFriend(int userId, int friendId) {

    }

    @Override
    public void removeFriend(int userId, int friendId) {

    }

    @Override
    public List<Friendship> findFriends(int userId) {
        return null;
    }
}
