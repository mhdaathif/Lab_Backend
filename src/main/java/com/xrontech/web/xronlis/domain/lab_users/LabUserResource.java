package com.xrontech.web.xronlis.domain.lab_users;

// import com.xrontech.web.xronlis.domain.lab.Lab;
// import com.xrontech.web.xronlis.domain.lab.LabUpdateDTO;
import com.xrontech.web.xronlis.domain.security.entity.User;
import com.xrontech.web.xronlis.dto.ApplicationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lab-user")
public class LabUserResource {
    private final LabUserService labUserService;

    // add labs - for admin
    @PostMapping("/add")
    public ResponseEntity<ApplicationResponseDTO> addLabUser(@Valid @RequestBody LabUserRegDTO labUserRegDTO) {
        return ResponseEntity.ok(labUserService.addLabUser(labUserRegDTO));
    }

    // update lab user - for admin && own profile as a lab user
    @PutMapping("/update/{lab-user-id}")
    public ResponseEntity<ApplicationResponseDTO> updateLabUser(@PathVariable("lab-user-id") Long labUserId, @Valid @RequestBody LabUserUpdateDTO labUserUpdateDTO) {
        return ResponseEntity.ok(labUserService.updateLabUser(labUserId, labUserUpdateDTO));
    }

    // change status of lab user - for admin
    @PutMapping("/status-change/{lab-user-id}")
    public ResponseEntity<ApplicationResponseDTO> statusChange(@PathVariable("lab-user-id") Long labUserId) {
        return ResponseEntity.ok(labUserService.statusChange(labUserId));
    }

    // delete lab user - for admin
    @DeleteMapping("/delete/{lab-user-id}")
    public ResponseEntity<ApplicationResponseDTO> deleteLabUser(@PathVariable("lab-user-id") Long labUserId) {
        return ResponseEntity.ok(labUserService.deleteLabUser(labUserId));
    }

    // get single lab user - for admin && own profile as a lab user
    @GetMapping("/get/{lab-user-id}")
    public ResponseEntity<User> getLabUserById(@PathVariable("lab-user-id") Long labUserId) {
        return ResponseEntity.ok(labUserService.getLabUserById(labUserId));
    }

}
