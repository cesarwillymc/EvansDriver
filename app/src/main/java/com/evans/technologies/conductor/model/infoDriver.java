package com.evans.technologies.conductor.model;

import com.evans.technologies.conductor.model.notification.notification;

import java.util.ArrayList;
import java.util.List;

public class infoDriver {
    Information information;
    boolean ok;
    ArrayList<notification> Notificacion;
    String message;
    String version;


    public ArrayList<notification> getNotificacion() {
        return Notificacion;
    }

    public void setNotificacion(ArrayList<notification> notificacion) {
        Notificacion = notificacion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }
}
