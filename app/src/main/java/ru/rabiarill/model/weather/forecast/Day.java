package ru.rabiarill.model.weather.forecast;

public class Day {
    private double temp_avg;
    private String icon;

    public double getTemp_avg() {
        return temp_avg;
    }

    public void setTemp_avg(double temp_avg) {
        this.temp_avg = temp_avg;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
