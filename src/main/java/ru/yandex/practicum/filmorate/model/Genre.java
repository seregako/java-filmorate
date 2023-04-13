package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    int id;
    String name;

    public Genre(int id) {
        this.id = id;
    }

    public Genre() {
    }
}
