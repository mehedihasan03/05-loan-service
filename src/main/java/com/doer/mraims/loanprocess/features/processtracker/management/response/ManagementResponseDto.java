package com.doer.mraims.loanprocess.features.processtracker.management.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementResponseDto {

    private String management_process_id;
    private String office_id;
    private String office_name_bn;
    private String office_name_en;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date business_date;
    private String businessDay;
    private String btnCreateTransactionEnabled;
    private List<String> samityIdList;
    private String userMessage;
}
