package com.example.hack3r.farmguide.objects;

public class Crop {
    String crop_title, soil, rainfall, temperature, image;

    public Crop(String title, String soil, String rainfall, String temperature, String image){
        this.crop_title = title;
        this.soil = soil;
        this.rainfall = rainfall;
        this.temperature = temperature;
        this.image = image;
    }

    public String getCrop_title() {
        return crop_title;
    }

    public String getImage() {
        return image;
    }

    public String getRainfall() {
        return rainfall;
    }

    public String getSoil() {
        return soil;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setCrop_title(String crop_title) {
        this.crop_title = crop_title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRainfall(String rainfall) {
        this.rainfall = rainfall;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
