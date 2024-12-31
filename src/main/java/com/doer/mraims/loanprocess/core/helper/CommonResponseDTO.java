package com.doer.mraims.loanprocess.core.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDTO {
    private boolean status;
    private int code;
    private String message;
}
