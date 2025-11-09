package com.xrontech.web.xronlis.domain.lab;

import com.xrontech.web.xronlis.dto.ApplicationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lab")
public class LabResource {
    private final LabService labService;

    // add labs - for admin
    @PostMapping("/add")
    public ResponseEntity<ApplicationResponseDTO> addLab(@Valid @RequestBody LabRegDTO labRegDTO) {
        return ResponseEntity.ok(labService.addLab(labRegDTO));
    }

    // update labs - for admin
    @PutMapping("/update/{lab-id}")
    public ResponseEntity<ApplicationResponseDTO> updateLab(@PathVariable("lab-id") Long labId, @Valid @RequestBody LabUpdateDTO labUpdateDTO) {
        return ResponseEntity.ok(labService.updateLab(labId, labUpdateDTO));
    }

    // change status of labs - for admin
    @PutMapping("/status-change/{lab-id}")
    public ResponseEntity<ApplicationResponseDTO> statusChange(@PathVariable("lab-id") Long labId) {
        return ResponseEntity.ok(labService.statusChange(labId));
    }

    // delete labs - for admin
    @DeleteMapping("/delete/{lab-id}")
    public ResponseEntity<ApplicationResponseDTO> deleteLab(@PathVariable("lab-id") Long labId) {
        return ResponseEntity.ok(labService.deleteLab(labId));
    }

    // get all labs - for admin
    @GetMapping("/get")
    public ResponseEntity<Page<Lab>> getLabs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return ResponseEntity.ok(labService.getLabs(page, size));
    }

    // get single lab - for admin & lab users
    // @GetMapping("/get/{lab-id}")
    // public ResponseEntity<Lab> getLabById(@PathVariable("lab-id") Long labId) {
    //     return ResponseEntity.ok(labService.getLabById(labId));
    // }

    // get filtered labs - for admin
    // @GetMapping("/get/filter")
    // public ResponseEntity<List<Lab>> getFilteredLabs(
    //         @RequestParam(required = false) String name,
    //         @RequestParam(required = false) String branch,
    //         @RequestParam(required = false) String fromDate,
    //         @RequestParam(required = false) String toDate,
    //         @RequestParam(required = false) Boolean status,
    //         @RequestParam(required = false) Boolean delete,
    //         @RequestParam(required = false) String mobile,
    //         @RequestParam(required = false) String code,
    //         @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
    //         @RequestParam(required = false, defaultValue = "asc") String sortDirection
    // ) {
    //     List<Lab> labs = labService.getFilteredLabs(
    //             name, branch,
    //             fromDate, toDate,
    //             status, delete, mobile, code, sortBy, sortDirection
    //     );
    //     return ResponseEntity.ok(labs);
    // }
}
