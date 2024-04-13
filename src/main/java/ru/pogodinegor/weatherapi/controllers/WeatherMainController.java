package ru.pogodinegor.weatherapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pogodinegor.weatherapi.exception.CustomWeatherFetchException;
import ru.pogodinegor.weatherapi.service.WeatherOneDayService;
import ru.pogodinegor.weatherapi.service.WeatherWeekService;
import ru.pogodinegor.weatherapi.types.WeatherServiceType;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Tag(name = "WeatherMain Controller", description = "Контроллер для получения прогноза погоды со всех api.")
@RestController
@RequestMapping(value = "/weather")
@RequiredArgsConstructor
public class WeatherMainController {
    private final WeatherOneDayService weatherOneDayService;
    private final WeatherWeekService weatherWeekService;

    @Operation(summary = "Прогноз погоды", description = "Прогноз погоды на текущий день.")
    @GetMapping("/one")
    public ResponseEntity<?> getWeatherOneDay(@RequestParam(defaultValue = "Belgorod") String city, @RequestParam String service) {
        validateServiceName(service);

        if ("all".equalsIgnoreCase(service)) {
            return weatherOneDayService.handleAllServicesOneDay(city);
        } else {
            return weatherOneDayService.handleSingleServiceOneDay(city, service);
        }
    }

    private static void validateServiceName(String service) {
        Optional<WeatherServiceType> matchedService = Arrays.stream(WeatherServiceType.values())
                .filter(s -> s.getServiceName().equalsIgnoreCase(service))
                .findFirst();

        if (!matchedService.isPresent() && !"all".equalsIgnoreCase(service)) {
            log.error("Данного сервиса не существует.");
            throw new CustomWeatherFetchException("Данного сервиса не существует.");
        }
    }



    @Operation(summary = "Прогноз погоды", description = "Прогноз погоды на неделю.")
    @GetMapping("/week")
    public ResponseEntity<?> getWeatherWeek(@RequestParam(defaultValue = "Belgorod") String city, @RequestParam String service) {

        validateServiceName(service);
        if ("all".equals(service)) {
            return weatherWeekService.handleAllServicesWeek(city);
        } else {
            return weatherWeekService.handleSingleServiceWeek(city, service);
        }
    }
}
