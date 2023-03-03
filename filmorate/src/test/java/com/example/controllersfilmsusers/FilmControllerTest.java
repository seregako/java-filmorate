package com.example.controllersfilmsusers;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @AfterEach
    public void clear() {
        controller.getStore().deletAllFilms();
        controller.setId(0);

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
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class, () -> controller.putFilm(inValidFilm));
        assertEquals("There is no film with id " + inValidFilm.getId(), ex.getMessage());
    }
}