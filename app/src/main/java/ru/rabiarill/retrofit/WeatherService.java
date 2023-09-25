package ru.rabiarill.retrofit;

import android.content.Context;
import android.widget.Toast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.rabiarill.BuildConfig;
import ru.rabiarill.R;
import ru.rabiarill.model.weather.WeatherData;
import ru.rabiarill.view_model.WeatherViewModel;

public class WeatherService {

    private final WeatherAPI weatherAPI;
    private final Context context;
    private WeatherViewModel viewModel;
    private static final String API_TOKEN = BuildConfig.API_KEY;


    public WeatherService(WeatherAPI weatherAPI, Context context, WeatherViewModel weatherViewModel) {
        this.weatherAPI = weatherAPI;
        this.context = context;
        this.viewModel = weatherViewModel;
    }

    public void getWeatherData(double lat, double lon, String lang) {
        int forecastLimit = 4;
        Call<WeatherData> call = weatherAPI.getWeather(lat, lon, lang, forecastLimit, API_TOKEN);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    WeatherData weatherData = response.body();
                    viewModel.setWeatherData(Objects.requireNonNull(weatherData));
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
