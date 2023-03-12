package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.strorage.InMemoryUserStorage;

import java.util.*;

@Service
public class UserService {
    InMemoryUserStorage storage;

    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(int userId, int friendId) {
        User user = storage.giveUser(userId);
        User friend = storage.giveUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        storage.updateUser(user);
        storage.updateUser(friend);
    }

    public void removeFromFriends(int userId, int friendId) {
        if (storage.getUsers().containsKey(userId) && storage.getUsers().containsKey(userId)) {
            storage.giveUser(userId).getFriends().remove(friendId);
            storage.giveUser(friendId).getFriends().remove(userId);
        }
    }

    public List<User> giveFriendsList(int userId) {
        List<User> friends = new ArrayList<>();
        for (Integer friendId : storage.giveUser(userId).getFriends()) {
            friends.add(storage.giveUser(friendId));
        }
        return friends;
    }

    public List<User> giveCommonFriends(int userId, int friendId) {
        List<Integer> commonFriendsId = new ArrayList<>();
        List<User> commonFriends = new ArrayList<>();
        for (int baseId : storage.giveUser(userId).getFriends()) {
            if (storage.giveUser(friendId).getFriends().contains(baseId)) {
                commonFriendsId.add(baseId);
            }
            for (Integer friend : commonFriendsId) {
                commonFriends.add(storage.giveUser(friend));
            }
        }
        return commonFriends;
    }
}
