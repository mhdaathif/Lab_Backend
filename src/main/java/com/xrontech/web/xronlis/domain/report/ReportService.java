package com.xrontech.web.xronlis.domain.report;

import com.xrontech.web.xronlis.domain.patient.OtpRequestDTO;
import com.xrontech.web.xronlis.domain.patient.Patient;
import com.xrontech.web.xronlis.domain.patient.PatientReportDTO;
import com.xrontech.web.xronlis.domain.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final PatientRepository patientRepository;
    private final ReportRepository reportRepository;

    public PatientReportDTO getPatientReport(String mobile) {
        Patient patient = patientRepository.findByMobile(mobile)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        Report report = reportRepository.findByPatientId(patient.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No report found for this patient"));

        return new PatientReportDTO(
                patient.getName(),
                patient.getMobile(),
                patient.getEmail(),
                patient.getAddress(),
                "https://your-app.com/reports/" + report.getReferenceNo(), // Assume this is the accessed link
                report.getReferenceNo(),
                report.getTestType().getName(),
                report.getPrice(),
                report.getLab().getName(),
                report.getCreatedAt()
        );
    }

}
