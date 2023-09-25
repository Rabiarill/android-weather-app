package ru.rabiarill.view_model;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.rabiarill.model.weather.WeatherData;

public class WeatherViewModel extends ViewModel {
    private MutableLiveData<WeatherData> weatherData = new MutableLiveData<>();

    public void setWeatherData(WeatherData data) {
        weatherData.setValue(data);
    }

    public LiveData<WeatherData> getWeatherData() {
        return weatherData;
    }
}
