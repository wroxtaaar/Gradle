package com.crio.session5.activity1.weather;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class WeatherUnlockedTest {

  @Mock
  private RestTemplate restTemplate;
  
  // TODO: CRIO_TASK_MODULE_IMPROVING_MAINTAINABILITY
  // Add enough unit tests for this module to cover all the cases
  // Minimum of 2 additiona unit tests should be added.
  
  @Test
  public void testRainExpected() throws IOException, URISyntaxException {
    Mockito.doReturn(readFile("bangalore_weather_unlocked.json"))
        .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.eq(String.class));
    Double latitude = 12.9762;
    Double longitude = 77.6033;

    LocalDateTime tripStartTime = LocalDateTime.of(2019,11,04,
        21, 15,00);
    LocalDateTime tripEndTime = LocalDateTime.of(2019,11,05,
        11, 45,00);

    WeatherApi weatherApi = new WeatherUnlocked(restTemplate);
    assertTrue(false == weatherApi.rainForecastAt(latitude, longitude,
        tripStartTime, tripEndTime));
  }

  private Object readFile(String fileName) throws IOException, URISyntaxException {
    URL pathUrl = getClass().getClassLoader().getResource(fileName);
    String response = new String(Files.readAllBytes(Paths.get(pathUrl.toURI())));
    return ResponseEntity.ok(response);
  }

}
