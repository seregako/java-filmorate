package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class CombinedGenre {
    int FilmId;
    int genreId;
    String genreName;
}
