package com.doer.mraims.loanprocess.core.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonListObjectResponseDTO<E> extends CommonResponseDTO {
    private List<E> data;
    private int total;
}
