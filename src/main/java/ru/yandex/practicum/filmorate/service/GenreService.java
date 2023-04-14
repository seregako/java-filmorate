package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.strorage.interfaces.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreStorage storage;

    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public void deleteByFilmId(int filmId){
        storage.removeByFilmId(filmId);
    }

    public List<Genre> getAll() {
        return storage.findAll();
    }

    public Genre getById(int genreId) {
        return storage.findById(genreId);
    }
}
