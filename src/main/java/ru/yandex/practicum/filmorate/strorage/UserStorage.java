package ru.yandex.practicum.filmorate.strorage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {
   void addUser(User user);

    void deletAllUsers();

    User giveUser(int userId);

    List<User> giveAllUsers();

    void updateUser(User user);
}
