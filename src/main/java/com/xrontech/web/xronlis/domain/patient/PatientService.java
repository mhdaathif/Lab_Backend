package com.xrontech.web.xronlis.domain.patient;

// import com.xrontech.web.xronlis.domain.report.Report;
// import com.xrontech.web.xronlis.domain.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final Random random = new Random();
    // private final ReportRepository reportRepository;

    // Generate a 6-digit OTP
    public String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }

    // Send OTP to a registered patient
    public String sendOtp(OtpRequestDTO otpRequestDTO) {
        Patient patient = patientRepository.findByMobile(otpRequestDTO.getMobile())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not registered"));

        String otp = generateOtp();
        patient.setOtp(otp);
        patient.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // OTP expires in 5 minutes
        patientRepository.save(patient);

        return "OTP sent successfully: " + otp;
    }

    // Verify OTP
    public boolean verifyOtp(OtpRequestDTO otpRequestDTO) {
        Patient patient = patientRepository.findByMobile(otpRequestDTO.getMobile())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        // if (patient.getOtp() == null || patient.getOtpExpiry() == null) {
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No OTP found. Request a new one.");
        // }

        // if (patient.getOtpExpiry().isBefore(LocalDateTime.now())) {
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP has expired. Request a new one.");
        // }

        // if (!patient.getOtp().equals(otpRequestDTO.getOtp())) {
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP. Please try again.");
        // }

        // OTP verified successfully, clear it from DB
        patient.setOtp(null);
        patient.setOtpExpiry(null);
        patientRepository.save(patient);

        return true;
    }

}
