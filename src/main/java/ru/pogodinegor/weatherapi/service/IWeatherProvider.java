package ru.pogodinegor.weatherapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface IWeatherProvider {
    JsonNode getWeatherResponseOneDay(String city) throws JsonProcessingException;

    JsonNode getWeatherResponseWeek(String city) throws JsonProcessingException;
}
