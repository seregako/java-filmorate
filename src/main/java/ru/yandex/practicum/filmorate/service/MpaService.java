package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.strorage.interfaces.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaStorage storage;

    public MpaService(MpaStorage storage) {
        this.storage = storage;
    }

    public List<Mpa> getAll() {
        return storage.findAll();
    }

    public Mpa getById(int mpaId) {
        return storage.findById(mpaId).orElseThrow(() -> new NoFoundException("no mpa with id " + mpaId));
    }
}
