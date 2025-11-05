package com.xrontech.web.xronlis.domain.security.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LabSelectionDTO {
    private Long userId;
    private Long labId;
}
