package ru.pogodinegor.weatherapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "WeatherApiApplication",
                description = "Микросервис 'WeatherApiApplication' предназначен для получения прогноза погоды. " +
                        "Он позволяет получать прогноз погоды за один день и неделю . ",

                version = "0.0.1",
                contact = @Contact(
                        name = "Pogodin Egor",
                        email = "666deadkain999@gmail.com",
                        url = "https://github.com/PogodinEgor"
                )
        )
)
public class WeatherApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiApplication.class, args);
    }

}
