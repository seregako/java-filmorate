package ru.yandex.practicum.filmorate.store;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserStore {
    private Map<Integer, User> users = new HashMap<>();

    public void addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    public void deletAllUsers() {
        users.clear();
    }

    public User giveUser(int userId) {
        return users.get(userId);
    }

    public List<User> giveAllUsers() {
        return users.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public void updateUser(User user) {
        if (users.containsKey(user.getId())){
        addUser(user);}
        else throw new IllegalArgumentException("There is no user with id " + user.getId());
    }

}
