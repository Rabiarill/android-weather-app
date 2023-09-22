package ru.rabiarill.model.weather.forecast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Forecast {
    private Date date;
    private Part parts;


    public String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Part getParts() {
        return parts;
    }

    public void setParts(Part parts) {
        this.parts = parts;
    }
}
