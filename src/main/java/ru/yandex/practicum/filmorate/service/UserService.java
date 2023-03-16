package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoUserIdException;
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
        if (userStorage.exist(user.getId())) {
            userStorage.update(user);
        } else throw new NoUserIdException("users id is wrong");
    }

    public User getById(int userId) {
        return userStorage.findById(userId).orElseThrow(() -> new NoUserIdException("Wrong user Id"));
    }


    public void addFriend(int userId, int friendId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NoUserIdException("Wrong user Id"));
        User friend = userStorage.findById(friendId).orElseThrow(() -> new NoUserIdException("Wrong friend Id"));
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFromFriends(int userId, int friendId) {
        if (userStorage.exist(userId) && userStorage.exist(userId)) {
            userStorage.findById(userId).get().getFriends().remove(friendId);
            userStorage.findById(friendId).get().getFriends().remove(userId);
        } else throw new NoUserIdException("users or friends id is wrong");
    }

    public List<User> getFriendsList(int userId) {
        if (userStorage.exist(userId)) {
            List<User> friends = new ArrayList<>();
            for (Integer friendId : userStorage.findById(userId).get().getFriends()) {
                friends.add(userStorage.findById(friendId).get());
            }
            return friends;
        } else throw new NoUserIdException("users id is wrong");
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        if (userStorage.exist(userId) && userStorage.exist(userId)) {
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
        } else throw new NoUserIdException("users or friends id is wrong");
    }

    public List<User> getAll() {
        return userStorage.findAll();
    }
}
