package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.strorage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FilmService {
    InMemoryFilmStorage storage;

    public FilmService(InMemoryFilmStorage storage) {
        this.storage = storage;
    }
    public void addLike(int filmId, int userId){
        Film film = storage.giveFilm(filmId);
        film.getLikes().add(userId);
        storage.updateFilm(film);
    }

  public void removeLike(int filmId, int userId){
      Film film = storage.giveFilm(filmId);
      film.getLikes().remove(userId);
      storage.updateFilm(film);

  }

    public List<Film> givePopulareFilms(int count){
        List <Film> popularsList = new ArrayList<>(storage.getPopularFilms());
        int actualCount;
        if (popularsList.size()>=count){
            actualCount = count;
        }
        else {actualCount = popularsList.size();}
        List <Film> populars = new LinkedList<>();
        for (int i = 0;i<actualCount; i++){
            populars.add(popularsList.get(i));
        }
        return populars;
    }
}
