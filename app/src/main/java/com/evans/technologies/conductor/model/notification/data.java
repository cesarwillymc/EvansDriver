package com.evans.technologies.conductor.model.notification;

public class data {
    String userId;
    String viajeId;
    String startAddress;
    String destinationAddress;
    Double latitudeOrigen;
    Double longitudeOrigen;
    Double latitudeDestino;
    Double longitudeDestino;
    String travelRate;
    String city;
    String response;
    String body;
    String title;
    String chatId;
    String travelRateDiscount,price,pricediscount;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricediscount() {
        return pricediscount;
    }

    public void setPricediscount(String pricediscount) {
        this.pricediscount = pricediscount;
    }

    public String getTravelRateDiscount() {
        return travelRateDiscount;
    }

    public void setTravelRateDiscount(String travelRateDiscount) {
        this.travelRateDiscount = travelRateDiscount;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getViajeId() {
        return viajeId;
    }

    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Double getLatitudeOrigen() {
        return latitudeOrigen;
    }

    public void setLatitudeOrigen(Double latitudeOrigen) {
        this.latitudeOrigen = latitudeOrigen;
    }

    public Double getLongitudeOrigen() {
        return longitudeOrigen;
    }

    public void setLongitudeOrigen(Double longitudeOrigen) {
        this.longitudeOrigen = longitudeOrigen;
    }

    public Double getLatitudeDestino() {
        return latitudeDestino;
    }

    public void setLatitudeDestino(Double latitudeDestino) {
        this.latitudeDestino = latitudeDestino;
    }

    public Double getLongitudeDestino() {
        return longitudeDestino;
    }

    public void setLongitudeDestino(Double longitudeDestino) {
        this.longitudeDestino = longitudeDestino;
    }

    public String getTravelRate() {
        return travelRate;
    }

    public void setTravelRate(String travelRate) {
        this.travelRate = travelRate;
    }
}
