package ru.yandex.practicum.filmorate.model;

import java.util.Objects;

public class Genre {
    int id;
    String name;
    public Genre (int id){
        this.id = id;
        setName1(id);
    }

    public Genre() {
    }

    private void setName1(int id){
        switch (id){
            case (1): this.name = "Комедия";
            break;
            case (2): this.name = "Драма";
            break;
            case (3): this.name = "Мультфильм";
            break;
            case (4): this.name = "Триллер";
            break;
            case (5): this.name = "Документальный";
            break;
            case (6): this.name = "Боевик";
            break;
            }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return getId() == genre.getId() && Objects.equals(getName(), genre.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
