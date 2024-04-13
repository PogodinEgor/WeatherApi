package ru.pogodinegor.weatherapi.exception;

public class CustomWeatherFetchException extends RuntimeException{
    public CustomWeatherFetchException(String message) {
        super(message);
    }

    public CustomWeatherFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
