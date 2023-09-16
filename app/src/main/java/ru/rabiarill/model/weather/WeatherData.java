package ru.rabiarill.model.weather;

import java.util.List;

import ru.rabiarill.model.weather.fact.Fact;
import ru.rabiarill.model.weather.forecast.Forecast;
import ru.rabiarill.model.weather.geo.GeoObj;

public class WeatherData {
    private String now_dt;
    private GeoObj geo_object;
    private Fact fact;
    private List<Forecast> forecasts;

    public String getNow_dt() {
        return now_dt;
    }

    public void setNow_dt(String now_dt) {
        this.now_dt = now_dt;
    }

    public GeoObj getGeo_object() {
        return geo_object;
    }

    public void setGeo_object(GeoObj geo_object) {
        this.geo_object = geo_object;
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }
}