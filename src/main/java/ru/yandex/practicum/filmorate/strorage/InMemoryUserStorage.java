package ru.yandex.practicum.filmorate.strorage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();

    private int id;

    public void addUser(User user) {
        id++;
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        users.put(id, user);
    }

    public void deletAllUsers() {
        users.clear();
    }

    public User giveUser(int userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        }
        else throw new IllegalArgumentException("there is no user with id "+userId);
    }

    public List<User> giveAllUsers() {
        return users.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public void updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())){
        users.put(user.getId(), user);
        }
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public void setId(int id) {
        this.id = id;
    }
}
