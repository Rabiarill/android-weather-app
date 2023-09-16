package ru.rabiarill.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import ru.rabiarill.model.weather.WeatherData;

public interface WeatherAPI {
    @GET("v2/forecast")
    Call<WeatherData> getWeather(@Query("lat") double lat,
                                 @Query("lon") double lon,
                                 @Query("lang") String lang,
                                 @Query("limit") int limit,
                                 @Header("X-Yandex-API-Key") String token);
}
