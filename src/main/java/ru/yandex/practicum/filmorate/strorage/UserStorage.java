package ru.yandex.practicum.filmorate.strorage;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    User add(User user);

    void deleteAll();

    Map<Integer, User> getUsers();

    Optional<User> findById(int userId);

    List<User> findAll();

    User update(User user);

    boolean exist(int userId);

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    List<Friendship> findFriends(int userId);

}
