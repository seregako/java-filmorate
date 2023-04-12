package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus (HttpStatus.NOT_FOUND)
public class NoIdException extends RuntimeException{

    public NoIdException(String message) {
        super(message);
    }
}
