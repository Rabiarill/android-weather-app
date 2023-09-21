package ru.rabiarill.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private WeatherAPI weatherAPI;
    private static final String BASE_URL = "https://api.weather.yandex.ru/";

    public ApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherAPI = retrofit.create(WeatherAPI.class);
    }

    public WeatherAPI getWeatherAPI() {
        return weatherAPI;
    }
}
