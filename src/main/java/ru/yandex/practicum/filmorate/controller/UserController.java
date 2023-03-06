package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.store.UserStore;

import javax.validation.Valid;;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int id = 0;
    private final UserStore store;
    public UserController(UserStore store) {
        this.store = store;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return store.giveAllUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable("id") int userId) {
        return store.giveUser(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        id++;
        user.setId(id);
        addUser(user);
        log.info("Map after POST: {}" + store.giveAllUsers());
        return user;
    }

    @PutMapping()
    public User putUser(@RequestBody User user) {
        store.updateUser(user);
        log.info("Map after PUT: {}" + store.giveAllUsers());
        return user;
    }

    private void addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        store.addUser(user);
    }

    public void setId(int id) {
        this.id = id;//Необходимо для тестов
    }

    public UserStore getStore() {
        return store;
    }
}