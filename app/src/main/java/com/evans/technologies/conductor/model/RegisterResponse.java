package com.evans.technologies.conductor.model;

public class RegisterResponse {

     public  boolean success;
     public String msg;

    public RegisterResponse(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
