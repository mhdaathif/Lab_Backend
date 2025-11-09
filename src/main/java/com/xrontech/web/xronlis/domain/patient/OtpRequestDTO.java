package com.xrontech.web.xronlis.domain.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpRequestDTO {

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^(070|071|072|074|075|076|077|078)\\d{7}$", message = "Invalid Sri Lankan mobile number format")
    private String mobile;

    private String otp;
}
