package com.xrontech.web.xronlis.domain.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientReportDTO  {
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String accessedLink;
    private String reportReferenceNo;
    private String testType;
    private double price;
    private String labName;
    private LocalDateTime testDate;
}
