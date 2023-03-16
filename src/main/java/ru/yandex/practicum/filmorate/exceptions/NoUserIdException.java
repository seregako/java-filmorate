package ru.yandex.practicum.filmorate.exceptions;

public class NoUserIdException extends RuntimeException{
    public NoUserIdException(String message) {
        super(message);
    }
}
