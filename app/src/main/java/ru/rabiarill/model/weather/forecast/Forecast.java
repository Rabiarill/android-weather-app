package ru.rabiarill.model.weather.forecast;

public class Forecast {
    private String date;
    private Part parts;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Part getParts() {
        return parts;
    }

    public void setParts(Part parts) {
        this.parts = parts;
    }
}
