package com.xrontech.web.xronlis.domain.patient;

import com.xrontech.web.xronlis.dto.ApplicationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patient")
public class PatientResource {
    private final PatientService patientService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApplicationResponseDTO> sendOtp(@Valid @RequestBody OtpRequestDTO otpRequestDTO) {
        String response = patientService.sendOtp(otpRequestDTO);
        return ResponseEntity.ok(new ApplicationResponseDTO(HttpStatus.OK, "OTP_SENT", response));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApplicationResponseDTO> verifyOtp(@Valid @RequestBody OtpRequestDTO otpRequestDTO) {
        boolean isValid = patientService.verifyOtp(otpRequestDTO);
        if (isValid) {
            return ResponseEntity.ok(new ApplicationResponseDTO(HttpStatus.OK, "OTP_VERIFIED", "OTP verified successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ApplicationResponseDTO(HttpStatus.BAD_REQUEST, "INVALID_OTP", "Invalid OTP"));
        }
    }


}
