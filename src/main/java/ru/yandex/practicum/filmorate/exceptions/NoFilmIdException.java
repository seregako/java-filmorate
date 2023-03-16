package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus (HttpStatus.NOT_FOUND)
public class NoFilmIdException extends RuntimeException{

    public NoFilmIdException(String message) {
        super(message);
    }
}
