package com.doer.mraims.loanprocess.core.helper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ResponseBody
public class CommonObjectResponseDTO<E> extends CommonResponseDTO {
    private E data;
//    private JSONObject jsonObject;
//    private Map<String, Object> map;

    // Explicit constructor to handle superclass fields
    public CommonObjectResponseDTO(boolean status, int code, String message, E data) {
        super(status, code, message); // Initialize superclass fields
        this.data = data;
    }

//    public CommonObjectResponseDTO(boolean status, int code, String message, JSONObject jsonObject) {
//        super(status, code, message); // Initialize superclass fields
//        this.jsonObject = jsonObject;
//    }

//    public CommonObjectResponseDTO(boolean status, int code, String message, Map<String, Object> map) {
//        super(status, code, message); // Initialize superclass fields
//        this.map = map;
//    }
}