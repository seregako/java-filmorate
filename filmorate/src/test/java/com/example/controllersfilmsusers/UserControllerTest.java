package com.example.controllersfilmsusers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import java.ru.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.store.UserStore;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { FilmorateApplication.class })
@WebAppConfiguration
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
    UserStore store;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @AfterEach
    public void clear(){
        controller.getStore().deletAllUsers();
        controller.setId(0);

    }

    @Test
    void addValidUserTest() throws Exception {
        User validUser = new User("seregako@mail.ru","a1","a",LocalDate.of(1987,3,4));
        String validUserString =  mapper.writeValueAsString(validUser);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUserString));
        validUser.setId(1);
        log.info("users Map: "+controller.getAllUsers());
        assertEquals(Arrays.asList(validUser), controller.getAllUsers());
    }

    @SneakyThrows
    @Test
    void addInvalidEmailUserTest() {
        User inValidEmailUser = new User("seregako-mail.ru","a1","a",LocalDate.of(1987,3,4));
        String inValidUserString =  mapper.writeValueAsString(inValidEmailUser);
        System.out.println("body of inValid user: "+inValidUserString);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(inValidUserString));
        inValidEmailUser.setId(1);
        log.info("users Map: "+controller.getAllUsers());
        assertTrue(controller.getAllUsers().isEmpty());
    }

    @SneakyThrows
    @Test
    void addEmptyNameUserTest() {
        User emptyNameUser = new User("seregako@mail.ru","a1","",LocalDate.of(1987,3,4));
        String inValidUserString =  mapper.writeValueAsString(emptyNameUser);
        System.out.println("body of empty name user: "+emptyNameUser);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(inValidUserString));
        emptyNameUser.setId(1);
        assertTrue(!controller.getAllUsers().isEmpty());
        log.info("users Map: "+controller.getAllUsers());
        assertEquals(controller.getUser(1).getName(),controller.getUser(1).getLogin());
        //пустое имя заплонилось логином
    }

    @SneakyThrows
    @Test
    void putUserTest() {
        User validUser = new User("seregako@mail.ru","postedUser","a",LocalDate.of(1987,3,4));
        String validUserString =  mapper.writeValueAsString(validUser);
        User validUser1 = new User("seregako@mail.ru","puttedUser","b",LocalDate.of(1987,3,4));
        validUser1.setId(1);
        String validUser1String =  mapper.writeValueAsString(validUser1);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUserString));
        log.info("Test usersStore Map: "+store.getAllUsers());
        mockMvc.perform(put("/users")
                .contentType("application/json")
                .content(validUser1String));
        log.info("Test usersStore Map: "+store.getAllUsers());
        assertEquals(validUser1, store.getUser(1));
    }

}
