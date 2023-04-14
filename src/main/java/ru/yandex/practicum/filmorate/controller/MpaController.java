package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService service;

    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Mpa> getMpaList() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable("id") int mpaId) {
        return service.getById(mpaId);
    }
}


