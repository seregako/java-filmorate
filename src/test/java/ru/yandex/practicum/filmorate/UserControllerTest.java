package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.strorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.strorage.UserDBStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    UserController controller;

    @Autowired
    UserDBStorage userStorage;

    @Autowired
    UserService userService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @AfterEach
    public void clear() {
        userStorage.deleteAll();
    }

    @Test
    void addValidUserTest() throws Exception {
        User validUser = new User("seregako@mail.ru", "a1", "a", LocalDate.of(1987, 3, 4));
        String validUserString = mapper.writeValueAsString(validUser);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUserString));
        validUser.setId(4);
        log.info("users Map: {} ", controller.getAllUsers());
        assertEquals(Arrays.asList(validUser), controller.getAllUsers());
    }

    @SneakyThrows
    @Test
    void addInvalidEmailUserTest() {
        User inValidEmailUser = new User("seregako-mail.ru", "a1", "a", LocalDate.of(1987, 3, 4));
        String inValidUserString = mapper.writeValueAsString(inValidEmailUser);
        System.out.println("body of inValid user: " + inValidUserString);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(inValidUserString));
        inValidEmailUser.setId(1);
        log.info("users Map: {} ", controller.getAllUsers());
        assertTrue(controller.getAllUsers().isEmpty());
    }

    @SneakyThrows
    @Test
    void addEmptyNameUserTest() {
        User emptyNameUser = new User("seregako@mail.ru", "a1", "", LocalDate.of(1987, 3, 4));
        String inValidUserString = mapper.writeValueAsString(emptyNameUser);
        System.out.println("body of empty name user: " + emptyNameUser);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(inValidUserString));
        emptyNameUser.setId(1);
        assertTrue(!controller.getAllUsers().isEmpty());
        log.info("users Map: {} ", controller.getAllUsers());
        assertEquals(userService.getById(12).getName(), userService.getById(12).getLogin());
        //пустое имя заплонилось логином
    }

    @SneakyThrows
    @Test
    void putUserTest() {
        User validUser = new User("seregako@mail.ru", "postedUser", "a", LocalDate.of(1987, 3, 4));
        String validUserString = mapper.writeValueAsString(validUser);
        User validUser1 = new User("seregako@mail.ru", "puttedUser", "b", LocalDate.of(1987, 3, 4));
        validUser1.setId(5);
        String validUser1String = mapper.writeValueAsString(validUser1);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUserString));
        log.info("Test usersuserService Map: {} ", userService.getAll());
        mockMvc.perform(put("/users")
                .contentType("application/json")
                .content(validUser1String));
        log.info("Test usersuserService Map: {} ", userService.getAll());
        assertEquals(validUser1, userService.getById(5));
    }

    @SneakyThrows
    @Test
    void addFriendTest() {
        User validUser = new User("seregako@mail.ru", "1", "a", LocalDate.of(1987, 3, 4));
        String validUserString = mapper.writeValueAsString(validUser);
        User validUser1 = new User("seregako@mail.ru", "2", "b", LocalDate.of(1987, 3, 4));
        String validUser1String = mapper.writeValueAsString(validUser1);
        User validUser2 = new User("seregako@mail.ru", "3", "c", LocalDate.of(1987, 3, 4));
        String validUser2String = mapper.writeValueAsString(validUser2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUserString));
        log.info("Test usersuserService Map: {} ", userService.getAll());
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUser1String));
        log.info("Test usersuserService Map: {} ", userService.getAll());
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUser2String));
        log.info("Test usersuserService Map: {} ", userService.getAll());
        mockMvc.perform(put("/users/1/friends/2")
                .contentType("application/json")
                .content(validUserString));
        mockMvc.perform(put("/users/2/friends/1")
                .contentType("application/json")
                .content(validUserString));
        mockMvc.perform(put("/users/2/friends/3")
                .contentType("application/json")
                .content(validUserString));
        mockMvc.perform(put("/users/3/friends/2")
                .contentType("application/json")
                .content(validUserString));
        log.info("Test usersuserService Map after friendAdd: {} ", userService.getAll());
        List<Integer> friends = new ArrayList<>();
        friends.add(1);
        friends.add(3);
        Assertions.assertEquals(userService.getCommonFriends(3,1), userService.getFriendsList(1));
    }

    @SneakyThrows
    @Test
    void deleteFriendTest() {
        User validUser = new User("seregako@mail.ru", "1", "a", LocalDate.of(1987, 3, 4));
        String validUserString = mapper.writeValueAsString(validUser);
        User validUser1 = new User("seregako@mail.ru", "2", "b", LocalDate.of(1987, 3, 4));
        String validUser1String = mapper.writeValueAsString(validUser1);
        User validUser2 = new User("seregako@mail.ru", "3", "c", LocalDate.of(1987, 3, 4));
        String validUser2String = mapper.writeValueAsString(validUser2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUserString));
        log.info("Test usersuserService Map: {} ", userService.getAll());
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUser1String));
        log.info("Test usersuserService Map: {} ", userService.getAll());
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUser2String));
        log.info("Test usersuserService Map: {} ", userService.getAll());
        mockMvc.perform(put("/users/9/friends/10")
                .contentType("application/json")
                .content(validUserString));
        log.info("Test usersuserService Map after friendAdd: {} ", userService.getAll());
        mockMvc.perform(delete("/users/10/friends/9")
                .contentType("application/json")
                .content(validUserString));
        log.info("Test usersuserService Map after deleteFriend: {} ", userService.getAll());
        List<Integer> friends = new ArrayList<>();
        Assertions.assertEquals(userService.getFriendsList(10).size(), friends.size());
    }

    @SneakyThrows
    @Test
    void getCommonFriendsTest() {
        User validUser = new User("seregako@mail.ru", "1", "a", LocalDate.of(1987, 3, 4));
        String validUserString = mapper.writeValueAsString(validUser);
        User validUser1 = new User("seregako@mail.ru", "2", "b", LocalDate.of(1987, 3, 4));
        String validUser1String = mapper.writeValueAsString(validUser1);
        User validUser2 = new User("seregako@mail.ru", "3", "c", LocalDate.of(1987, 3, 4));
        String validUser2String = mapper.writeValueAsString(validUser2);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUserString));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUser1String));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUser2String));
        mockMvc.perform(put("/users/6/friends/7")
                .contentType("application/json")
                .content(validUserString));
        mockMvc.perform(put("/users/7/friends/6")
                .contentType("application/json")
                .content(validUserString));
        mockMvc.perform(put("/users/7/friends/8")
                .contentType("application/json")
                .content(validUserString));
        mockMvc.perform(put("/users/8/friends/7")
                .contentType("application/json")
                .content(validUserString));
        log.info("Test usersuserService Map after deleteFriend: {} ", userService.getAll());
        mockMvc.perform(put("/users/6/friends/8")
                .contentType("application/json")
                .content(validUserString));
        log.info("Test usersuserService Map after deleteFriend: {} ", userService.getAll());
        List<User> commonFriends = new ArrayList<>();
        commonFriends.add(userService.getById(8));
        Assertions.assertEquals(userService.getCommonFriends(6, 7), commonFriends);
    }
}
