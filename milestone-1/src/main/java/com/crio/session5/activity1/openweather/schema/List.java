package com.crio.session5.activity1.openweather.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class List {
    public int dt;
    public Main main;
    public java.util.List<Weather> weather;
    public Clouds clouds;
    public Wind wind;
    public Rain rain;
    public Sys sys;
    public String dt_txt;
}
