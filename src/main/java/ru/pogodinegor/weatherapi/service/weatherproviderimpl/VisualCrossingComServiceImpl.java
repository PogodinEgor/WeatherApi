package ru.pogodinegor.weatherapi.service.weatherproviderimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ru.pogodinegor.weatherapi.exception.CustomWeatherFetchException;
import ru.pogodinegor.weatherapi.service.IWeatherProvider;

@Slf4j
@Service("visualCrossingComService")
@RequiredArgsConstructor
public class VisualCrossingComServiceImpl implements IWeatherProvider {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${weather.service.visualcrossing.key}")
    private String apiKey;

    @Value("${weather.service.visualcrossing..url}")
    private String apiUrl;


    public JsonNode getWeatherResponseOneDay(String city) {
        String url = String.format("%s/%s/today?unitGroup=metric&key=%s&contentType=json", apiUrl, city, apiKey);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<String>() {
                    }
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                return rootNode;
            } else {
                log.error("Failed to fetch weather data: " + response.getStatusCode() + " " + response.getBody());
                throw new CustomWeatherFetchException("Failed to fetch weather data: Status " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP Error: " + ex.getStatusCode() + " " + ex.getResponseBodyAsString());
            throw new CustomWeatherFetchException("HTTP Error: " + ex.getMessage(), ex);
        } catch (JsonProcessingException ex) {
            System.err.println("JSON parsing error: " + ex.getMessage());
            throw new CustomWeatherFetchException("JSON parsing error", ex);
        }
    }



    public JsonNode getWeatherResponseWeek(String city) {
        String url = String.format("%s/%s/next7days?unitGroup=metric&key=%s&contentType=json", apiUrl, city, apiKey);
        try{
            ResponseEntity<String> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<String>() {}
            );


        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return objectMapper.readTree(response.getBody());
        }else {
            log.error("Error: " + response.getStatusCode().value() + " " + response.getBody());
            throw new CustomWeatherFetchException("Error: " + response.getStatusCode().value() + " " + response.getBody());
        }
    }catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP Error: " + ex.getStatusCode() + " " + ex.getResponseBodyAsString());
            throw new CustomWeatherFetchException("HTTP Error: " + ex.getMessage(), ex);
        } catch (JsonProcessingException ex) {
            log.error("JSON parsing error: " + ex.getMessage());
            throw new CustomWeatherFetchException("JSON parsing error", ex);
        }
    }
}
