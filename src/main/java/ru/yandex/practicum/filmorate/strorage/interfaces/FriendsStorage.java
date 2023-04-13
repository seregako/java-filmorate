package ru.yandex.practicum.filmorate.strorage.interfaces;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendsStorage {

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    List<Friendship> findFriends(int userId);
}
