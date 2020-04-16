package com.anupom.covidinfo;

public class InfoWindowData {
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTotal_death() {
        return total_death;
    }

    public void setTotal_death(String total_death) {
        this.total_death = total_death;
    }

    public String getTotal_affected() {
        return total_affected;
    }

    public void setTotal_affected(String total_affected) {
        this.total_affected = total_affected;
    }

    public String getTotal_recovered() {
        return total_recovered;
    }

    public void setTotal_recovered(String total_recovered) {
        this.total_recovered = total_recovered;
    }

    String district, total_death, total_affected, total_recovered;
}
