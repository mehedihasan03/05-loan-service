package com.doer.mraims.loanprocess.core.exceptions;

public class CommonObjectResponseDTOV2<E> {

    private boolean status;
    private int code;
    private String message;
    private E data;

    public CommonObjectResponseDTOV2() {
    }

    public CommonObjectResponseDTOV2(boolean status, int code, String message, E data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
