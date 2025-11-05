package com.xrontech.web.xronlis.domain.report;

import com.xrontech.web.xronlis.domain.patient.OtpRequestDTO;
import com.xrontech.web.xronlis.domain.patient.PatientReportDTO;
import com.xrontech.web.xronlis.domain.patient.PatientService;
import com.xrontech.web.xronlis.dto.ApplicationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class ReportResource {
    private final ReportService reportService;
    //UserRole LabUser
    @GetMapping("/get/{mobile}")
    public ResponseEntity<PatientReportDTO> getPatientReport(@PathVariable String mobile) {
        PatientReportDTO patientReport = reportService.getPatientReport(mobile);
        return ResponseEntity.ok(patientReport);
    }
    //UserRole Patient
    @GetMapping("/get")
    public ResponseEntity<PatientReportDTO> getPatientReport(@PathVariable String mobile) {
        PatientReportDTO patientReport = reportService.getPatientReport(mobile);
        return ResponseEntity.ok(patientReport);
    }
}
