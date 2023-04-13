package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.strorage.interfaces.MpaStorage;

import java.util.List;

@Validated
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaStorage storage;

    public MpaController(MpaStorage storage) {
        this.storage = storage;
    }

    @GetMapping()
    public List<Mpa> getMpaList() {
        return storage.findAll();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable("id") int mpaId) {
        return storage.findById(mpaId);
    }
}


