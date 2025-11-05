package com.xrontech.web.xronlis.domain.lab;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LabRegDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotBlank(message = "Mobile is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number. It should be a 10-digit number.")
    private String mobile;

    @NotBlank
    @Email
    private String email;

//    @NotBlank(message = "Address is required")
    private String address;

}
