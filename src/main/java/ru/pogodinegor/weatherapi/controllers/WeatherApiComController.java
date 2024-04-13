package ru.pogodinegor.weatherapi.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pogodinegor.weatherapi.service.weatherproviderimpl.WeatherApiComServiceImpl;

@Tag(name = "WeatherApiCom Controller", description = "Контроллер для получения прогноза погоды с WeatherApiCom.")
@RestController
@RequestMapping(value = "/first_weather")
@RequiredArgsConstructor
public class WeatherApiComController {
    private final WeatherApiComServiceImpl firstWeatherService;

    @Operation(summary = "Прогноз погоды", description = "Прогноз погоды на текущий день.")
    @GetMapping("/one")
    public ResponseEntity<?> getWeatherResponseOneDay(@RequestParam(defaultValue = "Belgorod") String city)  {
       var response = firstWeatherService.getWeatherResponseOneDay(city);
       return ResponseEntity.ok(response);
    }

    @Operation(summary = "Прогноз погоды", description = "Прогноз погоды на неделю.")
    @GetMapping("/week")
    public ResponseEntity<?> getWeatherResponseWeek(@RequestParam(defaultValue = "Belgorod") String city) {
        var response =  firstWeatherService.getWeatherResponseWeek(city);
        return ResponseEntity.ok(response);
    }
}
