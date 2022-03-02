package com.crio.session5.activity1.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherTest {

  @Mock
  private RestTemplate restTemplate;
  // TODO: CRIO_TASK_MODULE_IMPROVING_MAINTAINABILITY
  // Add enough unit tests for this module to cover all the cases
  // Minimum of 2 additiona unit tests should be added.

  //  clear sky : 2019-11-04T21:00
  //  clear sky : 2019-11-05T00:00
  //  clear sky : 2019-11-05T03:00
  //  clear sky : 2019-11-05T06:00
  //  scattered clouds : 2019-11-05T09:00
  @Test
  public void noRainExpectedDuringTrip() throws IOException, URISyntaxException {

    Mockito.doReturn(readFile("bangalore_open_weather.json"))
        .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.eq(String.class));
    WeatherApi weatherApi = new OpenWeather(restTemplate);
    Double latitude = 12.9762;
    Double longitude = 77.6033;

    LocalDateTime tripStartTime = LocalDateTime.of(2019, 11, 04,
        21, 15, 00);
    LocalDateTime tripEndTime = LocalDateTime.of(2019, 11, 05,
        11, 45, 00);
    boolean result = weatherApi.rainForecastAt(latitude, longitude,
        tripStartTime, tripEndTime);
    assertFalse(result);
  }

  //  light rain : 2019-11-04T12:00
  //  light rain : 2019-11-04T15:00
  //  light rain : 2019-11-04T18:00
  //  clear sky : 2019-11-04T21:00
  //  clear sky : 2019-11-05T00:00
  //  clear sky : 2019-11-05T03:00
  //  clear sky : 2019-11-05T06:00
  //  scattered clouds : 2019-11-05T09:00
  //  light rain : 2019-11-05T12:00
  //  light rain : 2019-11-05T15:00
  @Test
  public void rainExpectedDuringTheTrip() throws IOException, URISyntaxException {
    Mockito.doReturn(readFile("bangalore_open_weather.json"))
        .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.eq(String.class));
    WeatherApi weatherApi = new OpenWeather(restTemplate);
    Double latitude = 12.9762;
    Double longitude = 77.6033;

    LocalDateTime tripStartTime = LocalDateTime.of(2019, 11, 04,
        12, 15, 00);
    LocalDateTime tripEndTime = LocalDateTime.of(2019, 11, 05,
        14, 00, 00);

    boolean result = weatherApi.rainForecastAt(latitude, longitude,
        tripStartTime, tripEndTime);
    Mockito.verify(restTemplate,times(1)).getForEntity(Mockito.anyString(), Mockito.eq(String.class));
    assertTrue(result);
  }

  private Object readFile(String fileName) throws IOException, URISyntaxException {
    URL pathUrl = getClass().getClassLoader().getResource(fileName);
    String response = new String(Files.readAllBytes(Paths.get(pathUrl.toURI())));
    return ResponseEntity.ok(response);
  }

  @Test
  public void maxTemperatureInNext15Days()  throws IOException, URISyntaxException {
    // Find max temperature for a trip based on lat, lon, startDate and endDate
    // TODO - Start with writing test for the designated functionality, and move onto implementation.
      Mockito.doReturn(readFile("bangalore_open_weather.json"))
              .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.eq(String.class));
      WeatherApi weatherApi = new OpenWeather(restTemplate);
      Double latitude = 12.9762;
      Double longitude = 77.6033;
  
      LocalDateTime tripStartTime = LocalDateTime.of(2019, 11, 04,
              12, 15, 00);
      LocalDateTime tripEndTime = LocalDateTime.of(2019, 11, 05,
              14, 00, 00);
  
      Double result = weatherApi.maxTemperature(latitude, longitude,
              tripStartTime, tripEndTime);
      Mockito.verify(restTemplate,times(1)).getForEntity(Mockito.anyString(), Mockito.eq(String.class));
      assertEquals(301.82, result, 0.01);
  }


}
