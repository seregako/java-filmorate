package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.strorage.interfaces.FriendsStorage;
import ru.yandex.practicum.filmorate.strorage.interfaces.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public UserService(@Qualifier("userDBStorage") UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public User create(User user) {
        loginValidator(user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    public User update(User user) {
        loginValidator(user);
        if (!userStorage.exist(user.getId())) {
            throw new NoFoundException("users id is wrong");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    public User getById(int userId) {
        return userStorage.findById(userId).orElseThrow(() -> new NoFoundException("Wrong user Id"));
    }


    public void addFriend(int userId, int friendId) {
        if (!(userStorage.exist(userId) && userStorage.exist(friendId))) {
            throw new NoFoundException("Wrong user Id");
        }
        friendsStorage.addFriend(userId, friendId);
    }

    public void removeFromFriends(int userId, int friendId) {
        if (!(userStorage.exist(userId) && userStorage.exist(friendId))) {
            throw new NoFoundException("Wrong user Id");
        }
        friendsStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriendsList(int userId) {
        if (!userStorage.exist(userId)) {
            throw new NoFoundException("users id is wrong");
        }
        List<User> friends = new ArrayList<>();
        if (friendsStorage.findFriends(userId).size() > 0) {
            for (Friendship friendship : friendsStorage.findFriends(userId)) {
                friends.add(userStorage.findById(friendship.getFriendId()).get());
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        if (!userStorage.exist(userId) && !userStorage.exist(friendId)) {
            throw new NoFoundException("users or friends id is wrong");
        }
        List<User> usersFriends = getFriendsList(userId);
        List<User> friendsFriends = getFriendsList(friendId);
        return usersFriends.stream().filter(friendsFriends::contains).collect(Collectors.toList());

    }

    void loginValidator(User user) {
        if (user.getLogin().contains(" ")) throw new IllegalArgumentException("логин не должен содержать пробелов");
    }

    public List<User> getAll() {
        return userStorage.findAll();
    }
}
