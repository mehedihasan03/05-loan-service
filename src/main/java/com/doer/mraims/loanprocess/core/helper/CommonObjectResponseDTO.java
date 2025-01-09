package com.doer.mraims.loanprocess.core.helper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ResponseBody
public class CommonObjectResponseDTO<E> extends CommonResponseDTO {
    private E data;

    public CommonObjectResponseDTO(boolean status, int code, String message, E data) {
        super(status, code, message); // Initialize superclass fields
        this.data = data;
    }
}