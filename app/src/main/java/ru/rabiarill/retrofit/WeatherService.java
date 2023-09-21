package ru.rabiarill.retrofit;

import android.content.Context;
import android.widget.Toast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.rabiarill.BuildConfig;
import ru.rabiarill.R;
import ru.rabiarill.listener.WeatherDataListener;
import ru.rabiarill.model.weather.WeatherData;

public class WeatherService {

    private final WeatherAPI weatherAPI;
    private final Context context;
    private final WeatherDataListener weatherDataListener;
    private static final String API_TOKEN = BuildConfig.API_KEY;


    public WeatherService(WeatherAPI weatherAPI, Context context, WeatherDataListener weatherDataListener) {
        this.weatherAPI = weatherAPI;
        this.context = context;
        this.weatherDataListener = weatherDataListener;
    }

    public void getWeatherData(double lat, double lon, String lang) {
        int forecastLimit = 4;
        Call<WeatherData> call = weatherAPI.getWeather(lat, lon, lang, forecastLimit, API_TOKEN);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    WeatherData weatherData = response.body();
                    weatherDataListener.updateUI(Objects.requireNonNull(weatherData));
                } else {
                    Toast.makeText(context, context.getString(R.string.err_msg_recive), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.err_msg_recive) + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
