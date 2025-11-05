package com.xrontech.web.xronlis.domain.lab_users;

import com.xrontech.web.xronlis.domain.lab.*;
import com.xrontech.web.xronlis.domain.security.entity.User;
import com.xrontech.web.xronlis.domain.security.entity.UserRole;
import com.xrontech.web.xronlis.domain.security.repos.UserRepository;
import com.xrontech.web.xronlis.domain.user.UserService;
import com.xrontech.web.xronlis.dto.ApplicationResponseDTO;
import com.xrontech.web.xronlis.exception.ApplicationCustomException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabUserService {

    private final LabRepository labRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private final UserService userService;
    private final LabUserRepository labUserRepository;

    public String generatePassword() {
        byte[] randomBytes = new byte[12];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
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


    public ApplicationResponseDTO addLabUser(@Valid LabUserRegDTO labUserRegDTO) {

        if (userRepository.findByEmail(labUserRegDTO.getEmail()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXIST", "Email already exist");
        }
        if (userRepository.findByMobile(labUserRegDTO.getMobile()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "MOBILE_ALREADY_EXIST", "Mobile already exist");
        }
        if (!labUserRegDTO.getPassword().equals(labUserRegDTO.getConfirmPassword())) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CONFIRM_PASSWORD_NOT_MATCHED", "Confirm password not matched");
        }
        // lab validation
        if (labRepository.findById(labUserRegDTO.getLabId()).isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "LAB_NOT_FOUND", "Lab not found");
        }
//        //  need to enable while adding multiple admins to same lab
//        if (labUserRepository.findByLabIdAndUser_UserRole(labUserRegDTO.getLabId(), UserRole.LAB_USER).isPresent()) {
//            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ANOTHER_USER_ALREADY_REGISTERED_TO_LAB", "Another User already registered to lab");
//        }
//        //  need to enable while adding multiple admins to same lab
//        if (labUserRepository.findByLabIdAndUser_UserRole(labUserRegDTO.getLabId(), UserRole.LAB_ADMIN).isPresent()) {
//            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "LAB_ADMIN_ALREADY_REGISTERED_TO_LAB", "Lab admin already registered to lab");
//        }

        User user = new User();
        user.setName(labUserRegDTO.getName());
        user.setEmail(labUserRegDTO.getEmail());
        user.setPassword(passwordEncoder.encode(labUserRegDTO.getPassword()));
        user.setMobile(labUserRegDTO.getMobile());
        user.setUserRole(UserRole.valueOf(labUserRegDTO.getUserRole()));
        user.setStatus(true);
        user.setDelete(false);

        String codeString = generateHashedUUID();
        user.setCode(codeString);

        LabUser labUser = new LabUser();
        labUser.setUser(user);
        labUser.setLab(labRepository.findById(labUserRegDTO.getLabId()).get());

        userRepository.save(user);
        labUserRepository.save(labUser);
        log.info("Lab user added Success: " + codeString);

//        String generatePassword = generatePassword();
//        mailService.sendAccountCredentialMail("User Account Credentials", authRequestDTO.getEmail(), authRequestDTO.getName(), generatePassword);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "LAB_USER_REGISTERED_SUCCESSFULLY", "Lab user registered successfully!");

    }


    public ApplicationResponseDTO updateLabUser(Long labId, @Valid LabUserUpdateDTO labUserUpdateDTO) {
        User user = userRepository.findByIdAndStatusAndDelete(labId, true, false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "LAB_USER_NOT_FOUND", "Lab user not found"));
        Long userId = user.getId();

        // unauthorized to change admin properties
        if (user.getUserRole().equals(UserRole.ADMIN)) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found");
        }

        if (userRepository.findByMobileAndIdNot(labUserUpdateDTO.getMobile(), userId).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "MOBILE_EXIST", "Mobile already exist");
        }

        if (labUserUpdateDTO.getMobile() != null) {
            user.setMobile(labUserUpdateDTO.getMobile());
        }
        if (labUserUpdateDTO.getName() != null) {
            user.setName(labUserUpdateDTO.getName());
        }
        if (labUserUpdateDTO.getAddress() != null) {
            user.setAddress(labUserUpdateDTO.getAddress());
        }

        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "LAB_USER_UPDATED_SUCCESSFULLY", "Lab user updated successfully!");
    }

    public ApplicationResponseDTO statusChange(Long labUserId) {
        User user = userRepository.findByIdAndStatusAndDelete(labUserId, true, false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "LAB_USER_NOT_FOUND", "Lab user not found"));

        if (userService.getCurrentUser().getUserRole().equals(UserRole.ADMIN)) {
            if (user.getStatus() == true) {
                user.setStatus(false);
            } else if (user.getStatus() == false) {
                user.setStatus(true);
            }
            userRepository.save(user);
        }
        return new ApplicationResponseDTO(HttpStatus.OK, "LAB_USER_STATUS_CHANGED_SUCCESSFULLY", "Lab user status changed successfully!");

    }

    public ApplicationResponseDTO deleteLabUser(Long labUserId) {
        User user = userRepository.findByIdAndStatusAndDelete(labUserId, true, false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "LAB_USER_NOT_FOUND", "Lab user not found"));

        user.setDeletedAt(LocalDateTime.now());
        user.setDelete(true);
        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "LAB_USER_DELETED_SUCCESSFULLY", "Lab user deleted successfully!");
    }

    public User getLabUserById(Long labUserId) {
        Optional<User> user = userRepository.findByIdAndStatusAndDeleteAndUserRoleNot(labUserId, true, false,UserRole.ADMIN);
        if (user.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "LAB_USER_NOT_FOUND", "Lab user not found");
        }
        return user.get();

    }

}
