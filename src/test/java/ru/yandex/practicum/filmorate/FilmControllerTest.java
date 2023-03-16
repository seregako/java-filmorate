package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NoFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.strorage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FilmorateApplication.class})
@WebAppConfiguration
public class FilmControllerTest {
    ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    FilmController controller;

    @Autowired
    @Qualifier("inMemoryFilmStorage")
    FilmStorage storage;

    @Autowired
    FilmService service;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @AfterEach
    public void clear() {
        storage.clearStorage();
        storage.setId(0);

    }

    @SneakyThrows
    @Test
    void addValidFilmTest() {
        Film validFilm = new Film("A", "a1", LocalDate.of(1987, 3, 4), 90);
        String validFilmString = mapper.writeValueAsString(validFilm);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(validFilmString));
        validFilm.setId(1);
        log.info("films Map: " + controller.getAllFilms());
        List<Film> oneFilmList = new ArrayList<>();
        oneFilmList.add(validFilm);
        assertEquals(oneFilmList, controller.getAllFilms());
    }

    @SneakyThrows
    @Test
    void addBlankNameFilmTest() {
        Film invalidFilm = new Film(" ", "a1", LocalDate.of(1987, 3, 4), 90);
        String validFilmString = mapper.writeValueAsString(invalidFilm);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(validFilmString));
        invalidFilm.setId(1);
        log.info("films Map: " + controller.getAllFilms());
        assertTrue(controller.getAllFilms().isEmpty());
    }

    @SneakyThrows
    @Test
    void addTooLongDescriptionFilmTest() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 201;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        Film invalidFilm = new Film("A", generatedString, LocalDate.of(1987, 3, 4), 90);
        String invalidFilmString = mapper.writeValueAsString(invalidFilm);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(invalidFilmString));
        invalidFilm.setId(1);
        log.info("films Map: " + controller.getAllFilms());
        assertTrue(controller.getAllFilms().isEmpty());
    }

    @SneakyThrows
    @Test
    void addWrongReleaseDateFilmTest() {
        Film invalidFilm = new Film("A", "a", LocalDate.of(1895, 12, 27), 90);
        String invalidFilmString = mapper.writeValueAsString(invalidFilm);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(invalidFilmString));
        invalidFilm.setId(1);
        log.info("films Map: " + controller.getAllFilms());
        assertTrue(controller.getAllFilms().isEmpty());
    }

    @SneakyThrows
    @Test
    void addNegativeDurationFilmTest() {
        Film invalidFilm = new Film("A", "a", LocalDate.of(1895, 12, 29), -90);
        String invalidFilmString = mapper.writeValueAsString(invalidFilm);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(invalidFilmString));
        invalidFilm.setId(1);
        log.info("films Map: " + controller.getAllFilms());
        assertTrue(controller.getAllFilms().isEmpty());
    }

    @SneakyThrows
    @Test
    void putFilmTest() {
        Film validFilm = new Film("A", "a1", LocalDate.of(1987, 3, 4), 90);
        String postedFilmString = mapper.writeValueAsString(validFilm);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(postedFilmString));
        Film validFilm1 = new Film("A1", "a11", LocalDate.of(1987, 3, 4), 90);
        validFilm1.setId(1);
        String putedFilmString1 = mapper.writeValueAsString(validFilm1);
        mockMvc.perform(put("/films")
                .contentType("application/json")
                .content(putedFilmString1));
        log.info("films Map: " + controller.getAllFilms());
        List<Film> oneFilmList = new ArrayList<>();
        oneFilmList.add(validFilm1);
        assertEquals(oneFilmList, controller.getAllFilms());
    }

    @SneakyThrows
    @Test
    void putToVoidIdFilmTest() {//Попробовал сделать тест на исключене через mockMvc, сам не понял, получилось или нет
        Film inValidFilm = new Film("A1", "a11", LocalDate.of(1987, 3, 4), 90);
        inValidFilm.setId(1);
        String putedFilmString1 = mapper.writeValueAsString(inValidFilm);
        try {
            mockMvc.perform(put("/films")
                    .contentType("application/json")
                    .content(putedFilmString1)).andExpect(mvcResult -> mvcResult.getResolvedException().getClass().
                    equals(IllegalArgumentException.class));
            log.info("films Map: " + controller.getAllFilms());
        } finally {
            return;
        }
    }

    @Test
    public void ThrowException() {//тестирование исключения без mockMvc
        Film inValidFilm = new Film("A", "a1", LocalDate.of(1987, 3, 4), 90);
        NoFilmIdException ex = Assertions.assertThrows(
                NoFilmIdException.class, () -> controller.putFilm(inValidFilm));
        assertEquals("Wrong film Id", ex.getMessage());
    }

    @SneakyThrows
    @Test
    void addLikeTest() {
        Film validFilm = new Film("A", "a1", LocalDate.of(1987, 3, 4), 90);
        String postedFilmString = mapper.writeValueAsString(validFilm);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(postedFilmString));
        Film validFilm1 = new Film("A1", "a11", LocalDate.of(1987, 3, 4), 90);
        String putedFilmString1 = mapper.writeValueAsString(validFilm1);
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(putedFilmString1));
        assertEquals(2, storage.findPopular().size());
        User validUser = new User("seregako@mail.ru", "a1", "a", LocalDate.of(1987, 3, 4));
        String validUserString = mapper.writeValueAsString(validUser);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUserString));
        validUser.setId(1);
        mockMvc.perform(put("/films/1/like/1").contentType("application/json"));
        assertEquals(service.getById(1).getRate(), 1);
        mockMvc.perform(delete("/films/1/like/1").contentType("application/json"));
        assertEquals(service.getById(1).getRate(), 0);
    }
}
