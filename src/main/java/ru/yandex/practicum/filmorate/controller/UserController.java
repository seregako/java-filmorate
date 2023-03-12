package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.strorage.InMemoryUserStorage;

import javax.validation.Valid;;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final InMemoryUserStorage store;
    private final UserService service;

    public UserController(InMemoryUserStorage store, UserService service) {
        this.store = store;
        this.service = service;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return store.giveAllUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable("id") int userId) {
        if (store.getUsers().containsKey(userId)) {
            return store.giveUser(userId);
        } else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "user not found"
        );
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        store.addUser(user);
        log.info("Map after POST: {}" + store.giveAllUsers());
        return user;
    }

    @PutMapping()
    public User putUser(@RequestBody User user) {
        if (store.getUsers().containsKey(user.getId())) {
            store.updateUser(user);
            log.info("Map after PUT: {}" + store.giveAllUsers());
            return user;
        } else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "there is no user with id " + user.getId()
        );
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        if (store.getUsers().containsKey(userId) && store.getUsers().containsKey(friendId)) {
            service.addFriend(userId, friendId);
            log.info("Map after add friend: {}" + store.giveAllUsers());
        } else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "there is no user with id "
        );
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        if (store.getUsers().containsKey(userId) && store.getUsers().containsKey(friendId)) {
            service.removeFromFriends(userId, friendId);
            log.info("Map after delete friend: {}" + store.giveAllUsers());
        } else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "there is no user with id "
        );
    }


    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int userId) {
        if (store.getUsers().containsKey(userId)) {
            return service.giveFriendsList(userId);
        } else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable("id") int userId, @PathVariable int otherId) {
        if (store.getUsers().containsKey(userId) && store.getUsers().containsKey(otherId)) {
            return new ResponseEntity<>(service.giveCommonFriends(userId, otherId), HttpStatus.OK);
        } else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }
}