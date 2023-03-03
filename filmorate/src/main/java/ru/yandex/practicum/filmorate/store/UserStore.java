package ru.yandex.practicum.filmorate.store;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserStore {
    private Map<Integer, User> users  = new HashMap<>();

    public void putUser (User user){
        if(user.getName()==null||user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    public void deleteUser(int userId){
        users.remove(userId);
    }

    public void deletAllUsers(){
        users.clear();
    }

    public User getUser(int userId) {
        return users.get(userId);
    }

    public Map<Integer, User> getAllUsers() {
        return users;
    }

    public void postUser(User user){
        users.put(user.getId(), user);
    }


}