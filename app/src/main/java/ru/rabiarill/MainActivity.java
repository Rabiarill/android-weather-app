package ru.rabiarill;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import ru.rabiarill.listener.WeatherDataListener;
import ru.rabiarill.model.weather.WeatherData;
import ru.rabiarill.model.weather.forecast.Day;
import ru.rabiarill.model.weather.forecast.Forecast;
import ru.rabiarill.retrofit.ApiClient;
import ru.rabiarill.retrofit.WeatherService;
import ru.rabiarill.view_model.WeatherViewModel;

public class MainActivity extends AppCompatActivity implements WeatherDataListener, LocationListener, SwipeRefreshLayout.OnRefreshListener {
    private WeatherViewModel viewModel;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView mainWeatherImage;
    TextView cityTV;
    TextView temperatureTV;
    TextView feelsLikeTV;
    TextView windSpeedTV;
    TextView humidityTV;
    List<CardView> cards;
    String systemLang;
    private WeatherService weatherService;
    private WeatherDataListener weatherDataListener;
    private double lat = 59.938; //by default lat and lon of Saint Petersburg
    private double lon = 30.314;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CODE_SETTINGS = 100;
    LocationManager locationManager;

    public MainActivity() {
    }

    public MainActivity(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe);
        mainWeatherImage = findViewById(R.id.main_weather_image);
        cityTV = findViewById(R.id.city);
        temperatureTV = findViewById(R.id.temperature);
        feelsLikeTV = findViewById(R.id.feels_like);
        windSpeedTV = findViewById(R.id.wind_speed);
        humidityTV = findViewById(R.id.humidity);
        cards = List.of(findViewById(R.id.card_1), findViewById(R.id.card_2), findViewById(R.id.card_3));
        swipeRefreshLayout.setOnRefreshListener(this);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        viewModel.getWeatherData().observe(this, new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {
                updateUI(weatherData);
            }
        });

        weatherDataListener = this;
        ApiClient apiClient = new ApiClient();
        weatherService = new WeatherService(apiClient.getWeatherAPI(), this, weatherDataListener);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        systemLang = Locale.getDefault().toString();

        updateWeather();
    }

    protected void updateWeather() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void updateUI(WeatherData weatherData) {
        updateFactWeather(weatherData);
        updateCards(weatherData);
    }

    private void updateFactWeather(WeatherData weatherData){
        cityTV.setText(MessageFormat.format("{0}, {1}", weatherData.getGeo_object().getLocality().getName(), weatherData.getGeo_object().getCountry().getName()));
        temperatureTV.setText(MessageFormat.format("{0}°C", weatherData.getFact().getTemp()));
        feelsLikeTV.setText(MessageFormat.format("{0} {1}°C", getString(R.string.feels_like), weatherData.getFact().getFeels_like()));
        windSpeedTV.setText(MessageFormat.format("{0} {1} {2}", getString(R.string.wind_speed), weatherData.getFact().getWind_speed(), getString(R.string.wind_speed_unit)));
        humidityTV.setText(MessageFormat.format("{0} {1}%", getString(R.string.humidity), weatherData.getFact().getHumidity()));
        updateImage(weatherData.getFact().getIcon(), mainWeatherImage);
    }

    private void updateCards(WeatherData weatherData) {
        TextView temperature;
        TextView date;
        ImageView image;
        int countDate = 1;

        for (CardView cardView : cards) {
            temperature = cardView.findViewById(R.id.card_temperature);
            date = cardView.findViewById(R.id.card_date);
            image = cardView.findViewById(R.id.card_image);

            Forecast forecast = weatherData.getForecasts().get(countDate);
            Day dayForecast = forecast.getParts().getDay();
            countDate++;

            temperature.setText(MessageFormat.format("{0}°C", dayForecast.getTemp_avg()));
            date.setText(forecast.getDate());
            updateImage(dayForecast.getIcon(), image);
        }
    }

    public void updateImage(String resourceName, ImageView imageViewToUpdate){
        resourceName = resourceName.replace("-", "_");
        Resources resources = getResources();
        final int resourceId = resources.getIdentifier(resourceName, "drawable",
                getPackageName());
        Glide
                .with(getApplicationContext())
                .load(resourceId)
                .error(R.drawable.load_err)
                .into(imageViewToUpdate);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                weatherService.getWeatherData(lat, lon, systemLang);
            } else {
                Toast.makeText(this, getString(R.string.geo_not_avalable_show_default_city), Toast.LENGTH_LONG).show();
                showSettingsDialog();
                weatherService.getWeatherData(lat, lon, systemLang);
            }
        }
    }

    @Override
    public void onLocationChanged(@androidx.annotation.NonNull Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        weatherService.getWeatherData(lat, lon, systemLang);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.settings));
        builder.setMessage(getString(R.string.settings_message));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_CODE_SETTINGS);
            }
        });
        builder.setNegativeButton(getString(R.string.no), null);
        builder.show();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            updateWeather();
            swipeRefreshLayout.setRefreshing(false);
        }, 1000);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
