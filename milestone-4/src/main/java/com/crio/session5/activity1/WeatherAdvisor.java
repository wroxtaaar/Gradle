package com.crio.session5.activity1;

/*
 * Copyright (c) Crio.Do 2019. All rights reserved
 */

import java.time.LocalDateTime;

import com.crio.session5.activity1.weather.WeatherApi;


// WeatherAdvisor is the class that can be useful to query about weather in a specific location.
public class WeatherAdvisor {

  WeatherApi weatherApi;

  public WeatherAdvisor(WeatherApi weatherApi) {
    this.weatherApi = weatherApi;
  }

  /**
   * Implement rain forecast at the give latitude, longitude for the trip duration.
   * You have to get the live weather forecast data for the given lat, long using
   * WeatherApi interface.
   * - If rain is expected at the specified geographic coordinates between
   * [tripStartTime, tripEndTime], return true.
   * - Else return false.
   *
   * @param latitude - Location latitude
   * @param longitude - Location longitude
   * @param tripStartsAt - Trip start time
   * @param tripEndsAt - Trip end time
   * @return boolean on whether rain is expected
   */
  boolean rainExpected(Double latitude, Double longitude,
    LocalDateTime tripStartsAt, LocalDateTime tripEndsAt) {
    return weatherApi.rainForecastAt(latitude, longitude, tripStartsAt, tripEndsAt);
  }
}
