package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.strorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.strorage.UserStorage;

import java.util.*;

@Service
public class UserService {
    @Qualifier("InMemoryUserStorage")
    UserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void post(User user) {
        userStorage.add(user);
    }

    public void put(User user) {
        if (!userStorage.exist(user.getId())) {throw new NoIdException("users id is wrong");}
            userStorage.update(user);
    }

    public User getById(int userId) {
        return userStorage.findById(userId).orElseThrow(() -> new NoIdException("Wrong user Id"));
    }


    public void addFriend(int userId, int friendId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NoIdException("Wrong user Id"));
        User friend = userStorage.findById(friendId).orElseThrow(() -> new NoIdException("Wrong friend Id"));
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFromFriends(int userId, int friendId) {
        if (!userStorage.exist(userId) && !userStorage.exist(userId)) {throw new NoIdException("users or friends id is wrong");}
            userStorage.findById(userId).get().getFriends().remove(friendId);
            userStorage.findById(friendId).get().getFriends().remove(userId);
    }

    public List<User> getFriendsList(int userId) {
        if (!userStorage.exist(userId)) {
            throw new NoIdException("users id is wrong");
        }
        List<User> friends = new ArrayList<>();
        for (Integer friendId : userStorage.findById(userId).get().getFriends()) {
            friends.add(userStorage.findById(friendId).get());
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        if (!userStorage.exist(userId) && !userStorage.exist(userId)) {throw new NoIdException("users or friends id is wrong");}
            List<Integer> commonFriendsId = new ArrayList<>();
            List<User> commonFriends = new ArrayList<>();
            for (int baseId : userStorage.findById(userId).get().getFriends()) {
                if (userStorage.findById(friendId).get().getFriends().contains(baseId)) {
                    commonFriendsId.add(baseId);
                }
                for (Integer friend : commonFriendsId) {
                    commonFriends.add(userStorage.findById(friend).get());
                }
            }
            return commonFriends;
    }

    public List<User> getAll() {
        return userStorage.findAll();
    }
}
