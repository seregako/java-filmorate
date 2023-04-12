package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable("id") int userId) {
        return userService.getById(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        User userFromstorage = userService.post(user);
        log.info("Map after POST: {}", userService.getAll());
        return userFromstorage;
    }

    @PutMapping()
    public User putUser(@RequestBody User user) throws NoIdException {
        User userFromstorage = userService.put(user);
        log.info("Map after PUT: {}", userService.getAll());
        return userFromstorage;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        userService.addFriend(userId, friendId);
        log.info("Map after add friend: {}", userService.getAll());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        userService.removeFromFriends(userId, friendId);
        log.info("Map after delete friend: {}", userService.getAll());
    }


    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int userId) {
        return userService.getFriendsList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int userId, @PathVariable int otherId) {
        return userService.getCommonFriends(userId, otherId);
    }
}