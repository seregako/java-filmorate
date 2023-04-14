package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class CombinedGenre {
   private int filmId;
   private int genreId;
   private String genreName;
}
