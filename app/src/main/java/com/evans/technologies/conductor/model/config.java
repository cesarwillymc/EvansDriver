package com.evans.technologies.conductor.model;

public class config {
    Boolean accountActivate,tripActivate;
    StatusFotos photosCar;
    Boolean informationCar;
    String response;

    public StatusFotos getPhotosCar() {
        return photosCar;
    }

    public void setPhotosCar(StatusFotos photosCar) {
        this.photosCar = photosCar;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getAccountActivate() {
        return accountActivate;
    }

    public void setAccountActivate(Boolean accountActivate) {
        this.accountActivate = accountActivate;
    }

    public Boolean getTripActivate() {
        return tripActivate;
    }

    public void setTripActivate(Boolean tripActivate) {
        this.tripActivate = tripActivate;
    }


    public Boolean getInformationCar() {
        return informationCar;
    }

    public void setInformationCar(Boolean informationCar) {
        this.informationCar = informationCar;
    }
}
