package com.doer.mraims.loanprocess.core.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CommonObjectResponseDTO<E> extends CommonResponseDTO {
    private E data;

    // Explicit constructor to handle superclass fields
    public CommonObjectResponseDTO(boolean status, int code, String message, E data) {
        super(status, code, message); // Initialize superclass fields
        this.data = data;
    }
}