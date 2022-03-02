package com.crio.session5.activity1;

/*
 * Copyright (c) Crio.Do 2019. All rights reserved
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

import com.crio.session5.activity1.log.UncaughtExceptionHandler;
import com.crio.session5.activity1.weather.OpenWeather;
import com.crio.session5.activity1.weather.WeatherUnlocked;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;

public class WeatherAdvisorApplication {



  // ./gradlew runWeather -q -Pargs=12.9762,77.6033,"2019-05-29 08:15","2019-05-29 09:15",v1
  // ./gradlew runWeather -q -Pargs=12.9762,77.6033,"2019-05-29 08:15","2019-05-29 09:15",v2
  // ./gradlew run --args="12.9762 77.6033 '2019-05-29 08:15' '2019-05-29 09:15' v1"
  // ./gradlew run --args="12.9762 77.6033 '2019-05-29 08:15' '2019-05-29 09:15' v2"
  // For a given period & for a given lat/long, answer whether rain is expected or not using
  // openweathermap or weatherunlocked API.
  // - If rain is expected, print 'Yes' on the console without quotes.
  // - Else print 'No' without quotes.
  //
  // To decide which weather API to choose, look at the last command line argument.
  // If it is:
  //  - v1, then use openweathermap API
  //  - v2, then use weatherunlocked API
  //
  // Steps:
  // 1. Reuse your previous module code for openweathermap API.
  // 2. Write code to add functionality for weatherunlocked API.
  // 3. Answer if rain is expected during the given trip time.
  //
  // @param args command line arguments
  //     args[0] - Latitude for which you want to find the weather
  //     args[1] - Longitude for which you want to find the weather
  //     args[2] - Trip start time
  //     args[3] - Trip end time
  //     args[4] - api version number as string ("v1" or "v2")

  public static void mainInterface(String[] args) {

    System.out.println(Arrays.toString(args));
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    Double latitude = Double.parseDouble(args[0]);
    Double longitude = Double.parseDouble(args[1]);

    LocalDateTime tripStartsAt = LocalDateTime.parse(args[2], dateTimeFormatter);
    LocalDateTime tripEndsAt = LocalDateTime.parse(args[3], dateTimeFormatter);

    String weatherApiVersion = args[4];
    RestTemplate restTemplate = new RestTemplate();

    WeatherAdvisor weatherAdvisor = null;
    if (weatherApiVersion.equals("v1")) {
      weatherAdvisor = new WeatherAdvisor(new OpenWeather());
    } else {
      weatherAdvisor = new WeatherAdvisor(new WeatherUnlocked());
    }

    if (weatherAdvisor.rainExpected(latitude, longitude, tripStartsAt, tripEndsAt)) {
      System.out.println("Yes");
    } else {
      System.out.println("No");
    }
  }


  public static void main(String[] args) {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());
   
    mainInterface(args);

  }
}

