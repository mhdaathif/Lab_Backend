package com.xrontech.web.xronlis.domain.lab_users;

import com.xrontech.web.xronlis.domain.lab.Lab;
import com.xrontech.web.xronlis.domain.security.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabUserRepository extends JpaRepository<LabUser, Long> {
    List<LabUser> findByUserId(Long user);
    List<LabUser> findByUser_Email(String email);
    List<LabUser> findByLab_Mobile(String mobile);

    Optional<LabUser> findByUserIdAndLabIdAndUser_UserRole(Long userId, Long labId,UserRole userUserRole);
    Optional<LabUser> findByLabIdAndUser_UserRole(Long labId,UserRole userUserRole);
}
