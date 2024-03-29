package ru.yandex.practicum.filmorate.strorage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
   void add(User user);

    void deleteAll();

    Map<Integer, User> getUsers();

    Optional <User> findById(int userId);

    List<User> findAll();

    void update(User user);

 public boolean exist (int userId);
}
