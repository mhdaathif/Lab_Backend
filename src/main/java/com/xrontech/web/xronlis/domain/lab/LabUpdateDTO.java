package com.xrontech.web.xronlis.domain.lab;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LabUpdateDTO {
    private String name;
    private String branch;
    private String mobile;
    private String email;
    private String address;
}
