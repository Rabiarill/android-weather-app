package ru.rabiarill.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.rabiarill.DateTypeAdapter;

public class ApiClient {
    private WeatherAPI weatherAPI;
    private static final String BASE_URL = "https://api.weather.yandex.ru/";
    private Gson gson;

    public ApiClient() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        weatherAPI = retrofit.create(WeatherAPI.class);
    }

    public WeatherAPI getWeatherAPI() {
        return weatherAPI;
    }
}
