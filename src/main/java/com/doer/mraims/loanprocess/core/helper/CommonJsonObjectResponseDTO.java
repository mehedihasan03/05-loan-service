package com.doer.mraims.loanprocess.core.helper;

import lombok.*;
import org.json.JSONObject;

@Getter
@Setter
public class CommonJsonObjectResponseDTO<T>  {
    private boolean status;
    private int code;
    private String message;
    private String stringData;
    private JSONObject data;


    public CommonJsonObjectResponseDTO(boolean status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public CommonJsonObjectResponseDTO(boolean status, Integer code, String message, JSONObject data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public CommonJsonObjectResponseDTO(boolean status, Integer code, String message, String stringData) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.stringData = stringData;
    }
}