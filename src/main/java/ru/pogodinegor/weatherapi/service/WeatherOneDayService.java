package ru.pogodinegor.weatherapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pogodinegor.weatherapi.types.WeatherServiceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherOneDayService {

    private final ApplicationContext context;
    private final ExecutorService executorService;


    public ResponseEntity<?> handleAllServicesOneDay(String city) {
        List<JsonNode> responses = Collections.synchronizedList(new ArrayList<>());
        String[] serviceNames = WeatherServiceType.getServiceNames();
        List<Callable<JsonNode>> tasks = createTasksForServicesOneDay(city, serviceNames);
        executeTasksOneDay(tasks, responses);
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<?> handleSingleServiceOneDay(String city, String service) {
        List<JsonNode> responses = new ArrayList<>();
        IWeatherProvider weatherProvider = (IWeatherProvider) context.getBean(service);
        try {
            JsonNode response = weatherProvider.getWeatherResponseOneDay(city);
            if (response != null) {
                responses.add(response);
            }
        } catch (Exception e) {
            log.error("Error calling service: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error retrieving weather data from " + service + ": " + e.getMessage());
        }
        return ResponseEntity.ok(responses);
    }

    public List<Callable<JsonNode>> createTasksForServicesOneDay(String city, String[] serviceNames) {
        List<Callable<JsonNode>> tasks = new ArrayList<>();
        for (String serviceName : serviceNames) {
            IWeatherProvider weatherProvider = (IWeatherProvider) context.getBean(serviceName);
            tasks.add(() -> {
                try {
                    return weatherProvider.getWeatherResponseOneDay(city);
                } catch (Exception e) {
                    log.error("Error calling " + serviceName + ": " + e.getMessage());
                    return null;
                }
            });
        }
        return tasks;
    }

    public void executeTasksOneDay(List<Callable<JsonNode>> tasks, List<JsonNode> responses) {
        try {
            List<Future<JsonNode>> futures = executorService.invokeAll(tasks);
            for (Future<JsonNode> future : futures) {
                try {
                    JsonNode result = future.get();
                    if (result != null) {
                        responses.add(result);
                    }
                } catch (ExecutionException e) {
                    log.error("Error in execution: " + e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
