package com.evans.technologies.conductor.model;

public class Driver {

    private String id,token,message;
    private Boolean imgUpdate,imgActivation,accountActivate;
    private String name, surname, email, city, cellphone, numDocument,  imageProfile,idPago;
    private Boolean estadoimg,acceptimg,estado;

   private String user,darMensaje;

    public String getDarMensaje() {
        return darMensaje;
    }

    public void setDarMensaje(String darMensaje) {
        this.darMensaje = darMensaje;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public Driver(String message) {
        this.message = message;
    }
    public Driver(String id, String token, String message) {
        this.id = id;
        this.token = token;
        this.message = message;
    }

    public Driver(String name, String surname, String email, String city, String celphone, String numDocument, Boolean accountActivate, String imageProfile) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.city = city;
        this.cellphone = celphone;
        this.numDocument = numDocument;
        this.accountActivate = accountActivate;
        this.imageProfile = imageProfile;
    }


    public String getIdPago() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getEstadoimg() {
        return estadoimg;
    }

    public void setEstadoimg(Boolean estadoimg) {
        this.estadoimg = estadoimg;
    }

    public Boolean getAcceptimg() {
        return acceptimg;
    }

    public void setAcceptimg(Boolean acceptimg) {
        this.acceptimg = acceptimg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String celphone) {
        this.cellphone = celphone;
    }

    public String getNumDocument() {
        return numDocument;
    }

    public void setNumDocument(String numDocument) {
        this.numDocument = numDocument;
    }

    public Boolean getAccountActivate() {
        return accountActivate;
    }

    public void setAccountActivate(Boolean accountActivate) {
        this.accountActivate = accountActivate;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
