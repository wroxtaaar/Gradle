package com.crio.session5.activity1.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


// Class that wraps functionality to connect to the weatherUnlocked API and get data back.

public class WeatherUnlocked implements WeatherApi {

  private RestTemplate restTemplate;
  

  public WeatherUnlocked(){
    
  }
  public WeatherUnlocked(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  private String buildWeatherUrl(Double lat, Double lon) {
    final String appId = "2054e475";
    final String appKey = "5d12d75426a488379dcf5c27b9bef6f7";
    return "http://api.weatherunlocked.com/api/forecast/" + lat.toString() + "," + lon.toString()
        + "?app_id=" + appId + "&app_key=" + appKey;
  }

  private String reFormatTime(String time) {
    if (time.length() == 4) {
      return time.substring(0, 2) + ":" + time.substring(2, 4);
    } else {
      return "0" + time.substring(0, 1) + ":" + time.substring(1, 3);
    }
  }

  private boolean forecastRainFromWeatherReport(JsonNode root, LocalDateTime tripStartsAt,
      LocalDateTime tripEndsAt) throws IOException {
    JsonNode days = root.path("Days");

    boolean possibleRain = false;
    if (days.isArray()) {
      for (final JsonNode day : days) {

        JsonNode timeframes = day.path("Timeframes");
        assert (true == timeframes.isArray());
        for (final JsonNode timeframe : timeframes) {

          String weatherDescription = timeframe.path("wx_desc").asText();

          DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
          String dateTime =
              timeframe.path("date").asText() + " " + reFormatTime(timeframe.path("time").asText());
          LocalDateTime startTimeOfWeatherInfo = LocalDateTime.parse(dateTime, dateTimeFormatter);
          LocalDateTime endTimeOfWeatherInfo = startTimeOfWeatherInfo.plusMinutes(179);

          boolean timeWindowOverlap = doesTimeWindowOverlap(tripStartsAt, tripEndsAt, possibleRain, weatherDescription,
                  startTimeOfWeatherInfo, endTimeOfWeatherInfo);
          if (timeWindowOverlap) {
            if (weatherDescription.contains("rain")) {
              possibleRain = true;
            }
          }
        }
      }
    }

    return possibleRain;
  }

  private boolean doesTimeWindowOverlap(LocalDateTime tripStartsAt, LocalDateTime tripEndsAt, boolean possibleRain,
                                 String weatherDescription, LocalDateTime startTimeOfWeatherInfo, LocalDateTime endTimeOfWeatherInfo) {
    boolean condition1 = startTimeOfWeatherInfo.isAfter(tripStartsAt)
        && startTimeOfWeatherInfo.isBefore(tripEndsAt);
    boolean condition2 = tripStartsAt.isAfter(startTimeOfWeatherInfo)
        && tripEndsAt.isBefore(endTimeOfWeatherInfo);
    return condition2 || condition1;
  }

  @Override
  public boolean rainForecastAt(Double lat, Double lon,
      LocalDateTime tripStartsAt, LocalDateTime tripEndsAt) {

    String weatherUrl = buildWeatherUrl(lat, lon);
    ResponseEntity<String> response = restTemplate.getForEntity(weatherUrl, String.class);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = null;
    boolean rainExpected = false;

    try {
      root = mapper.readTree(response.getBody());
      rainExpected = forecastRainFromWeatherReport(root, tripStartsAt, tripEndsAt);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return rainExpected;
  }

  @Override
  public Double maxTemperature(Double lat, Double lon, LocalDateTime tripStartsAt, LocalDateTime tripEndsAt) {
    throw new RuntimeException();
  }
}
