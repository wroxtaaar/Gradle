package com.crio.session5.activity1.weather;

import java.time.LocalDateTime;


public interface WeatherApi {
  /**
   * Implement rain forecast at the give latitude, longitude for the trip duration.
   * You have to get the live weather forecast data for the given lat, long using
   * WeatherApi interface.
   * - If rain is expected at the specified geographic coordinates between
   * [tripStartTime, tripEndTime], return true.
   * - Else return false.
   *
   * @param lat - Location latitude
   * @param lon - Location longitude
   * @param tripStartsAt - Trip start time
   * @param tripEndsAt - Trip end time
   * @return boolean on whether rain is expected
   */
  boolean rainForecastAt(Double lat, Double lon,
      LocalDateTime tripStartsAt, LocalDateTime tripEndsAt);

  Double maxTemperature(Double lat, Double lon,
      LocalDateTime tripStartsAt, LocalDateTime tripEndsAt);

}