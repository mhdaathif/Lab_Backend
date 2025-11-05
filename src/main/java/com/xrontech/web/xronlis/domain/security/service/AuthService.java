package com.xrontech.web.xronlis.domain.security.service;

import com.xrontech.web.xronlis.domain.lab.Lab;
import com.xrontech.web.xronlis.domain.lab.LabRepository;
import com.xrontech.web.xronlis.domain.security.dto.AuthResponseDTO;
import com.xrontech.web.xronlis.domain.security.dto.LogInDTO;
import com.xrontech.web.xronlis.domain.security.dto.ResetForgotPasswordDTO;
import com.xrontech.web.xronlis.domain.security.entity.User;
import com.xrontech.web.xronlis.domain.security.entity.UserRole;
import com.xrontech.web.xronlis.domain.security.repos.UserRepository;
import com.xrontech.web.xronlis.domain.security.rest.AuthRequestDTO;
import com.xrontech.web.xronlis.domain.security.rest.LabSelectionDTO;
import com.xrontech.web.xronlis.domain.security.util.JwtTokenUtil;
import com.xrontech.web.xronlis.domain.user.ResetPasswordDTO;
import com.xrontech.web.xronlis.domain.user.UserService;
import com.xrontech.web.xronlis.dto.ApplicationResponseDTO;
import com.xrontech.web.xronlis.exception.ApplicationCustomException;
import com.xrontech.web.xronlis.mail.MailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailService mailService;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private final LabRepository labRepository;

    public String generatePassword() {
        byte[] randomBytes = new byte[12];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public ApplicationResponseDTO signUp(AuthRequestDTO authRequestDTO) {
        if (userRepository.findByEmail(authRequestDTO.getEmail()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXIST", "Email already exist");
        }
        if (userRepository.findByMobile(authRequestDTO.getMobile()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "MOBILE_ALREADY_EXIST", "Mobile already exist");
        }

        if (!authRequestDTO.getPassword().equals(authRequestDTO.getConfirmPassword())) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CONFIRM_PASSWORD_NOT_MATCHED", "Confirm password not matched");
        }

//        String generatePassword = generatePassword();
//        mailService.sendAccountCredentialMail("User Account Credentials", authRequestDTO.getEmail(), authRequestDTO.getName(), generatePassword);


        ///////////////////////////////////////////////////
        User user = new User();
        user.setName(authRequestDTO.getName());
        user.setEmail(authRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));
        user.setMobile(authRequestDTO.getMobile());
        user.setStatus(true);
        user.setDelete(false);

//        String userRole = authRequestDTO.getUserRole();
//        if (userRole.equals("LAB_USER")) {
//            user.setUserRole(UserRole.LAB_USER);
//        } else if (userRole.equals("LAB_ADMIN")) {
//            user.setUserRole(UserRole.LAB_ADMIN); // need to update the code according yo the role of user needed to register
//        }

        return new ApplicationResponseDTO(HttpStatus.CREATED, "USER_REGISTERED_SUCCESSFULLY", "User registered successfully!");

    }

    public AuthResponseDTO login(LogInDTO logInDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(logInDTO.getEmail());
        if (optionalUser.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_EMAIL", "Invalid email");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(logInDTO.getPassword(), user.getPassword())) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_EMAIL_OR_PASSWORD", "Invalid email or password");
        }

        checkAccountStatus(user);

        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);
        return new AuthResponseDTO(HttpStatus.OK, "LOGIN_SUCCESS", "Login success", accessToken, refreshToken);

    }

    public ApplicationResponseDTO resetPassword(ResetPasswordDTO resetPasswordDTO) {
        User user = userService.findByEmail(userService.getCurrentUser().getEmail());
        if (!passwordEncoder.matches(resetPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_OLD_PASSWORD", "Invalid old password");
        } else if (!(resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword()))) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CONFIRM_PASSWORD_DOES_NOT_MATCH", "Confirm password does not match");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "USER_NEW_PASSWORD_UPDATED_SUCCESSFULLY", "User new password updated successfully!");
    }

    public AuthResponseDTO generateRefreshToken(String refreshToken) {
        if (jwtTokenUtil.validateToken(refreshToken)) {
            String email = jwtTokenUtil.getUsernameFromToken(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(user);
            String role = user.getUserRole().toString();
            return new AuthResponseDTO(HttpStatus.OK, "NEW_ACCESS_TOKEN_&_NEW_REFRESH_TOKEN", "New access & refresh token", accessToken, newRefreshToken);
        }
        throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "Invalid refresh token");
    }

    public ApplicationResponseDTO forgotPassword(String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "EMAIL_NOT_FOUND", "Email not found")
        );

        checkAccountStatus(user);

        String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
        String resetPasswordLink = baseUrl + "/auth/reset-password/" + user.getId();

        mailService.sendForgotPasswordMail("Reset password", email, user.getName(), resetPasswordLink);

        return new ApplicationResponseDTO(HttpStatus.OK, "FORGOT_PASSWORD_SENT_SUCCESSFULLY", "Forgot password link sent to your email ");
    }

    public void checkAccountStatus(User user) {
        if (user.getDelete()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ACCOUNT_DELETED", "Account deleted");
        }

        if (!user.getStatus()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ACCOUNT_DISABLED", "Account disabled");
        }
    }

    public ApplicationResponseDTO resetForgotPassword(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found")
        );
        checkAccountStatus(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "VALID_RESET_PASSWORD_LINK", "Valid reset password link!");
    }

    public ApplicationResponseDTO resetForgotPassword(Long id, ResetForgotPasswordDTO resetForgotPasswordDTO) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found")
        );
        checkAccountStatus(user);

        if (!(resetForgotPasswordDTO.getNewPassword().equals(resetForgotPasswordDTO.getConfirmPassword()))) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CONFIRM_PASSWORD_DOES_NOT_MATCH", "Confirm password does not match");
        }
        user.setPassword(passwordEncoder.encode(resetForgotPasswordDTO.getNewPassword()));
        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "USER_NEW_PASSWORD_UPDATED_SUCCESSFULLY", "User new password updated successfully!");
    }

}
