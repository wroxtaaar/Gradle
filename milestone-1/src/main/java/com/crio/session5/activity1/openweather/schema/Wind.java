package com.crio.session5.activity1.openweather.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind{
    public double speed;
    public int deg;
}

