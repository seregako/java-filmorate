package ru.yandex.practicum.filmorate.strorage.interfaces;


import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;
import java.util.Set;

public interface LikesStorage {
    void removeLike(int filmId, int userId);

    List<Like> getAll();

    Set<Integer> getLikesById(int filmId);

    void clearStorage();
}
