package com.xrontech.web.xronlis.domain.lab_users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LabUserUpdateDTO {
//    @NotBlank(message = "Name is required")
    private String name;
//    @NotBlank(message = "Mobile is required")
//    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number. It should be a 10-digit number.")
    private String mobile;
//    @NotBlank(message = "Address is required")
    private String address;
}
