package com.crio.session5.activity1.openweather.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {
    public double temp;
    public double temp_min;
    public double temp_max;
    public int pressure;
    public int sea_level;
    public int grnd_level;
    public int humidity;
    public double temp_kf;
}
