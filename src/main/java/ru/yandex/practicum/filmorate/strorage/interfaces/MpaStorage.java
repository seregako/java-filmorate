package ru.yandex.practicum.filmorate.strorage.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    Mpa findById(int mpaId);

    List<Mpa> findAll();
}


