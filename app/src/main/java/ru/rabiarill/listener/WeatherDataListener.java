package ru.rabiarill.listener;

import ru.rabiarill.model.weather.WeatherData;

public interface WeatherDataListener {
    void updateUI(WeatherData weatherData);
}
