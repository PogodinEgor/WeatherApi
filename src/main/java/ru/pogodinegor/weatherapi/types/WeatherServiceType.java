package ru.pogodinegor.weatherapi.types;


import java.util.Arrays;

public enum WeatherServiceType {
    WEATHER_API_COM_SERVICE("weatherApiComService"),
    VISUAL_CROSSING_COM_SERVICE("visualCrossingComService");


    private final String serviceName;

    WeatherServiceType(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public static String[] getServiceNames() {
        return Arrays.stream(WeatherServiceType.values())
                .map(WeatherServiceType::getServiceName)
                .toArray(String[]::new);
    }
}

