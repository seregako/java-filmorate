package ru.yandex.practicum.filmorate.strorage.interfaces;

import java.util.Set;

public interface LikesStorage {
    void removeLike(int filmId, int userId);

    Set<Integer> getLikesById(int filmId);
}
