package com.evans.technologies.conductor.model;

public class LoginResponse {
    public Boolean success;
    public String msg;
    public String message;
    public Driver user;
    private Referido referido;

    public Referido getReferido() {
        return referido;
    }

    public void setReferido(Referido referido) {
        this.referido = referido;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Driver getUser() {
        return user;
    }

    public void setUser(Driver user) {
        this.user = user;
    }
}
