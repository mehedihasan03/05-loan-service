package com.doer.mraims.loanprocess.features.processtracker.management.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagementRequestDto {

    @NotBlank(message = "Office ID cannot be null or empty.")
    private String officeId;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Business date must be in YYYY-MM-DD format.")
    private String businessDate;
    private String managementProcessId;
}
