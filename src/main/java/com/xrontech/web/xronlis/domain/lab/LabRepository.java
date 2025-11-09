package com.xrontech.web.xronlis.domain.lab;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabRepository extends JpaRepository<Lab, Long> {
    Optional<Lab> findByMobile(String mobile);
//    Optional<Lab> findByUserIdAndUser_UserRole(Long userId, UserRole userRole);
//    Optional<Lab> findByAdminIdAndAdmin_UserRole(Long adminId, UserRole userRole);
    Optional<Lab> findAllByNameIgnoreCaseAndBranchIgnoreCase(String name, String branch);
    Optional<Lab> findAllByNameIgnoreCaseAndBranchIgnoreCaseAndIdNot(String name, String branch, Long labId);

    Optional<Lab> findByIdAndDelete(Long id, boolean delete);

    Page<Lab> findAllByStatusAndDelete(boolean status, boolean delete, Pageable pageable);
    Page<Lab> findAllByNameAndStatusAndDelete(String labName, boolean status, boolean delete, Pageable pageable);
    Optional<Lab> findAllByNameAndStatusAndDelete(String labName, boolean status, boolean delete);

    Optional<Lab> findByIdAndStatusAndDelete(Long labId, boolean status, boolean delete);

    List<Lab> findAll(Specification<Lab> specification, Sort sort);

    Optional<Lab> findByIdAndMobile(Long labId, String mobile);

    Optional<Lab> findByMobileAndIdNot(String mobile, Long labId);

    Optional<Lab> findAllByEmailIgnoreCaseAndIdNot(String email, Long labId);


//    List<Lab> findByAdminId(Long userId);
}
