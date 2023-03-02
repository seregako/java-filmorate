package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.store.UserStore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserStore store;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    int id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        List <User> list = store.getAllUsers().entrySet().stream().map(e->e.getValue()).collect(Collectors.toList());
        return list;
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable("id") int userId) {
        return store.getUser(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        id++;
        user.setId(id);
        addUser(user);
        log.info("Map after POST" + store.getAllUsers());
        return user;
    }

    @PutMapping()
    public User putUser(@RequestBody User user) {
        if (store.getAllUsers().containsKey(user.getId())) {
            addUser(user);
            log.info("Map after PUT" + store.getAllUsers());
            return user;
        } else throw new IllegalArgumentException("There is no user with id " + user.getId());
    }

    private void addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        store.postUser(user);
    }

    public void setId(int id) {
        this.id = id;//Необходимо для тестов
    }

    public UserStore getStore() {
        return store;
    }
}

