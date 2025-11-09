package com.xrontech.web.xronlis;

import com.xrontech.web.xronlis.domain.lab.Lab;
import com.xrontech.web.xronlis.domain.lab.LabRepository;
import com.xrontech.web.xronlis.domain.lab_users.LabUser;
import com.xrontech.web.xronlis.domain.lab_users.LabUserRepository;
import com.xrontech.web.xronlis.domain.patient.Patient;
import com.xrontech.web.xronlis.domain.patient.PatientRepository;
import com.xrontech.web.xronlis.domain.security.entity.User;
import com.xrontech.web.xronlis.domain.security.entity.UserRole;
import com.xrontech.web.xronlis.domain.security.repos.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RequiredArgsConstructor
@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "XronLis", version = "2.0", description = "XronLis"))
//@SecurityScheme(name = "XronLis", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class XronLisApplication {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final LabRepository labRepository;
    private final LabUserRepository labUserRepository;

    public static void main(String[] args) {
        SpringApplication.run(XronLisApplication.class, args);
    }


    public static String generateHashedUUID() {
        try {
            UUID uuid = UUID.randomUUID();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(uuid.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.substring(0, 12);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init() {
        // admin
        if (userRepository.findByEmail("mhdjasir4565@gmail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .name("Jasir4565")
                    .email("mhdjasir4565@gmail.com")
                    .mobile("0762684595")
                    .password(passwordEncoder.encode("1234"))
                    .status(true)
                    .delete(false)
                    .userRole(UserRole.ADMIN)
                    .code(generateHashedUUID())
                    .build());
        }
        // lab 1
        if (labRepository.findByMobile("0762684597").isEmpty()) {
            labRepository.save(Lab.builder()
                    .name("ABC")
                    .branch("Mawanella")
                    .mobile("0762684597")
                    .email("abc@gmail.com")
                    .status(true)
                    .delete(false)
                    .address("No 213, Hemmathagama rd, Mawanella")
                    .code("LAB_" + generateHashedUUID())
                    .build());
        }
        //lab 2
        if (labRepository.findByMobile("0762684598").isEmpty()) {
            labRepository.save(Lab.builder()
                    .name("ABC")
                    .branch("Kandy")
                    .mobile("0762684598")
                    .email("abc@gmail.com")
                    .status(true)
                    .delete(false)
                    .address("No 213, Kandy rd, Pilimatalawa")
                    .code("LAB_" + generateHashedUUID())
                    .build());
        }
        //lab 3
        if (labRepository.findByMobile("0762684591").isEmpty()) {
            labRepository.save(Lab.builder()
                    .name("XYZ")
                    .branch("Mawanella")
                    .mobile("0762684591")
                    .email("xyz@gmail.com")
                    .status(true)
                    .delete(false)
                    .address("No 213, Hemmathagama rd, Mawanella")
                    .code("LAB_" + generateHashedUUID())
                    .build());
        }
        // lab user 1
        if (userRepository.findByEmail("james@gmail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .name("James")
                    .email("james@gmail.com")
                    .mobile("0762684599")
                    .password(passwordEncoder.encode("1234"))
                    .status(true)
                    .delete(false)
                    .userRole(UserRole.LAB_USER)
                    .code(generateHashedUUID())
                    .build());
        }
        // lab user 2
        if (userRepository.findByEmail("nimal@gmail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .name("Nimal")
                    .email("nimal@gmail.com")
                    .mobile("0762684590")
                    .password(passwordEncoder.encode("1234"))
                    .status(true)
                    .delete(false)
                    .userRole(UserRole.LAB_USER)
                    .code(generateHashedUUID())
                    .build());
        }
        // lab admin 3
        if (userRepository.findByEmail("monika@gmail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .name("Monika")
                    .email("monika@gmail.com")
                    .mobile("0762684596")
                    .password(passwordEncoder.encode("1234"))
                    .status(true)
                    .delete(false)
                    .userRole(UserRole.LAB_ADMIN)
                    .code(generateHashedUUID())
                    .build());
        }
        // lab user relationship foreign 1
        if (labUserRepository.findByUser_Email("james@gmail.com").isEmpty() && labUserRepository.findByLab_Mobile("0762684597").isEmpty()) {
            User user = userRepository.findByEmail("james@gmail.com").get();
            Lab lab = labRepository.findByMobile("0762684597").get();
            labUserRepository.save(LabUser.builder()
                    .lab(lab)
                    .user(user)
                    .build());

        }
        // lab user relationship foreign 2
        if (labUserRepository.findByUser_Email("nimal@gmail.com").isEmpty() && labUserRepository.findByLab_Mobile("0762684598").isEmpty()) {
            User user = userRepository.findByEmail("nimal@gmail.com").get();
            Lab lab = labRepository.findByMobile("0762684598").get();
            labUserRepository.save(LabUser.builder()
                    .lab(lab)
                    .user(user)
                    .build());
        }
        // lab user relationship foreign 3
        if (labUserRepository.findByUser_Email("monika@gmail.com").isEmpty() && labUserRepository.findByLab_Mobile("0762684591").isEmpty()) {
            User user = userRepository.findByEmail("monika@gmail.com").get();
            Lab lab = labRepository.findByMobile("0762684591").get();
            labUserRepository.save(LabUser.builder()
                    .lab(lab)
                    .user(user)
                    .build());
        }

        // patient
        if (patientRepository.findByMobile("0762684594").isEmpty()) {
            Long labId = labRepository.findByMobile("0762684597").get().getId();
            patientRepository.save(Patient.builder()
                    .name("James")
                    .email("james@gmail.com")
                    .mobile("0762684594")
                    .status(true)
                    .delete(false)
                    .labId(labId)
                    .otp("1234")
                    .build());
        }
    }
}
