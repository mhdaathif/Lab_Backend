package com.xrontech.web.xronlis.domain.lab;

import com.xrontech.web.xronlis.domain.lab_users.LabUserRegDTO;
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
public class LabService {

    private final LabRepository labRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private final UserService userService;

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

    public ApplicationResponseDTO addLab(@Valid LabRegDTO labRegDTO) {

        if (labRepository.findAllByNameIgnoreCaseAndBranchIgnoreCase(labRegDTO.getName(), labRegDTO.getBranch()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "LAB_ALREADY_EXIST", "Lab already exist");
        }
        if (labRepository.findByMobile(labRegDTO.getMobile()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "LAB_ALREADY_EXIST", "Lab already exist");
        }

        Lab lab = new Lab();
        lab.setName(labRegDTO.getName());
        lab.setBranch(labRegDTO.getBranch());
        lab.setMobile(labRegDTO.getMobile());
        lab.setEmail(labRegDTO.getEmail());
        lab.setAddress(labRegDTO.getAddress());
        lab.setDelete(false);
        lab.setStatus(true);

        String code = "LAB_" + generateHashedUUID();
        lab.setCode(code);
        labRepository.save(lab);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "LAB_ADDED_SUCCESSFULLY", "Lab added successfully!");
    }

    public ApplicationResponseDTO updateLab(Long labId, @Valid LabUpdateDTO labUpdateDTO) {
        Lab lab = labRepository.findByIdAndStatusAndDelete(labId, true, false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "LAB_NOT_FOUND", "Lab not found"));

        if (labRepository.findByMobileAndIdNot(labUpdateDTO.getMobile(), labId).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "LAB_MOBILE_EXIST", "Lab mobile already exist");
        }
        if (labRepository.findAllByNameIgnoreCaseAndBranchIgnoreCaseAndIdNot(labUpdateDTO.getName(),labUpdateDTO.getBranch(), labId).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "SAME_LAB_ALREADY_EXIST", "Same lab already exist");
        }
//        // email not a unique
//        if (labRepository.findAllByEmailIgnoreCaseAndIdNot(labUpdateDTO.getEmail(), labId).isPresent()) {
//            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "LAB_EMAIL_EXIST", "Lab email already exist");
//        }

        lab.setName(labUpdateDTO.getName());
        lab.setBranch(labUpdateDTO.getBranch());
        lab.setMobile(labUpdateDTO.getMobile());
        lab.setEmail(labUpdateDTO.getEmail());
        lab.setAddress(labUpdateDTO.getAddress());
        labRepository.save(lab);
        return new ApplicationResponseDTO(HttpStatus.OK, "LAB_UPDATED_SUCCESSFULLY", "Lab updated successfully!");
    }

    public ApplicationResponseDTO statusChange(Long labId) {
        Lab lab = labRepository.findByIdAndStatusAndDelete(labId, true, false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "LAB_NOT_FOUND", "Lab not found"));

        if (userService.getCurrentUser().getUserRole().equals(UserRole.ADMIN)) {
            if (lab.getStatus() == true) {
                lab.setStatus(false);
            } else if (lab.getStatus() == false) {
                lab.setStatus(true);
            }
            labRepository.save(lab);
        }
        return new ApplicationResponseDTO(HttpStatus.OK, "LAB_STATUS_CHANGED_SUCCESSFULLY", "Lab status changed successfully!");
    }

    public ApplicationResponseDTO deleteLab(Long labId) {
        Lab lab = labRepository.findByIdAndStatusAndDelete(labId, true, false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "LAB_NOT_FOUND", "Lab not found"));
        if (userService.getCurrentUser().getUserRole().equals(UserRole.ADMIN)) {
            lab.setDeletedAt(LocalDateTime.now());
            lab.setDelete(true);
            labRepository.save(lab);
        }
        return new ApplicationResponseDTO(HttpStatus.OK, "LAB_DELETED_SUCCESSFULLY", "Lab deleted successfully!");
    }

    public Page<Lab> getLabs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return labRepository.findAllByStatusAndDelete(true, false, pageable);
    }

    public Lab getLabById(Long labId) {
        Optional<Lab> lab = labRepository.findByIdAndStatusAndDelete(labId, true, false);
        if (lab.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "LAB_NOT_FOUND", "Lab not found");
        }
        return lab.get();
    }

    public List<Lab> getFilteredLabs(
            String name,
            String branch,
            String fromDate,
            String toDate,
            Boolean status,
            Boolean delete,
            String mobile,
            String code,
            String sortBy,
            String sortDirection) {

        Specification<Lab> specification = LabSpecification.filterByAttributes(
                name, branch,
                fromDate, toDate,
                status, delete, mobile, code
        );
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return labRepository.findAll(specification, sort);
    }
}
