package com.crio.session5.activity1.weather;

import com.crio.session5.activity1.openweather.schema.Root;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

// Class that wraps functionality to connect to the openweathermap API and get data back.

// TODO:
//  Refactor this class in such as way that it facilitates implementation of #maxTemperature
//  Points to remember while refactoring
//  1. Long methods
//  2. Variable/ method names
//  3. Code re-usability
//  4. cyclomatic complexity
//  5. Clean conditions (if/ switch)
//  6. Code readability
public class OpenWeather implements WeatherApi {

    public static final int THREE_HOURS_OFFSET = 179;
    public static final String OPEN_WEATHER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private RestTemplate restTemplate;

    public OpenWeather() {}

    public OpenWeather(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String buildWeatherUrl(Double lat, Double lon) {
        // FIXME - Is this the right way to build the Url?
        String appId = "7f7ff184d89d00f68c9440eb1fcf47a5";
        return "https://api.openweathermap.org/data/2.5/forecast"
                + "?lat=" + lat.toString() + "&lon=" + lon.toString() + "&appid=" + appId;
    }

    private boolean isGoingToRain(LocalDateTime tripStartDay, LocalDateTime tripEndDay,
                                  String description, LocalDateTime startTimeOfWeatherInfo) {
        LocalDateTime endTimeOfWeatherInfo = startTimeOfWeatherInfo.plusMinutes(THREE_HOURS_OFFSET);

        boolean doesTimeWindowOverlap = isDoesTimeWindowOverlap(tripStartDay, tripEndDay, startTimeOfWeatherInfo,
                endTimeOfWeatherInfo);

        return doesTimeWindowOverlap && description.contains("rain");
    }

    private boolean isDoesTimeWindowOverlap(LocalDateTime tripStartDay, LocalDateTime tripEndDay,
                                            LocalDateTime startTimeOfWeatherInfo, LocalDateTime endTimeOfWeatherInfo) {
        boolean startDateWithinTripRange = startTimeOfWeatherInfo.isAfter(tripStartDay)
                && startTimeOfWeatherInfo.isBefore(tripEndDay);
        boolean endDateWithinTripRange = tripStartDay.isAfter(startTimeOfWeatherInfo)
                && tripEndDay.isBefore(endTimeOfWeatherInfo);
        return startDateWithinTripRange || endDateWithinTripRange;
    }

    @Override
    public boolean rainForecastAt(Double lat, Double lon,
                                  LocalDateTime tripStartsAt, LocalDateTime tripEndsAt) {
        String weatherUrl = buildWeatherUrl(lat, lon);
        ResponseEntity<String> response = restTemplate.getForEntity(weatherUrl, String.class);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(OPEN_WEATHER_DATE_FORMAT);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Root rootElement = mapper.readValue(response.getBody(), Root.class);
            return rootElement.list.stream().anyMatch(list ->
                    isGoingToRain(tripStartsAt, tripEndsAt,
                            list.weather.get(0).description,
                            LocalDateTime.parse(list.dt_txt, dateTimeFormatter))
            );

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Double maxTemperature(Double lat, Double lon, LocalDateTime tripStartsAt, LocalDateTime tripEndsAt) {
        String weatherUrl = buildWeatherUrl(lat, lon);
        ResponseEntity<String> response = restTemplate.getForEntity(weatherUrl, String.class);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(OPEN_WEATHER_DATE_FORMAT);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Root rootElement = mapper.readValue(response.getBody(), Root.class);
            return rootElement.list.stream().filter(list -> {
                LocalDateTime startTimeOfWeatherInfo = LocalDateTime.parse(list.dt_txt, dateTimeFormatter);
                        return isThreeHourTimeWindoOverlap(tripStartsAt, tripEndsAt, startTimeOfWeatherInfo);
                    }).max(Comparator.comparingDouble(value -> value.main.temp_max))
                    .get().main.temp_max;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private boolean isThreeHourTimeWindoOverlap(LocalDateTime tripStartsAt, LocalDateTime tripEndsAt, LocalDateTime startTimeOfWeatherInfo) {
        LocalDateTime endTimeOfWeatherInfo = startTimeOfWeatherInfo.plusMinutes(THREE_HOURS_OFFSET);
        return isDoesTimeWindowOverlap(tripStartsAt, tripEndsAt, startTimeOfWeatherInfo, endTimeOfWeatherInfo);
    }
}