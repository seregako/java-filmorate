package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    int id;
    String name;

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mpa(int id) {
        this.id = id;
    }

    public Mpa() {
    }
}
