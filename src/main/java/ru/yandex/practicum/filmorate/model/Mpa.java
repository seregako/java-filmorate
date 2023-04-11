package ru.yandex.practicum.filmorate.model;

import lombok.Data;

//@Data
public class Mpa {
    int id;
    String name;

    public Mpa (int id){
        this.id = id;
        choiseName(id);
    }

    public Mpa() {
    }

    private void choiseName (int id){
        switch (id){
            case (1): this.name = "G";
            break;
            case (2): this.name = "PG";
            break;
            case (3): this.name = "PG-13";
            break;
            case (4): this.name = "R";
            break;
            case (5): this.name = "NC-17";
            break;
            default: throw new IllegalArgumentException("there is no mpa with id "+ id);
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
        return "Mpa{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
