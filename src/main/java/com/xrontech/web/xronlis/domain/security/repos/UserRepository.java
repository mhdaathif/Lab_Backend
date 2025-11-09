package com.xrontech.web.xronlis.domain.security.repos;

import aj.org.objectweb.asm.commons.Remapper;
import com.xrontech.web.xronlis.domain.lab.Lab;
import com.xrontech.web.xronlis.domain.security.entity.User;
import com.xrontech.web.xronlis.domain.security.entity.UserRole;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndStatusAndDelete(String email,boolean status, boolean delete);

    List<User> findAllByUserRole(UserRole userRole);

    Optional<User> findByMobile(String mobile);

    Optional<User> findTopByOrderByIdDesc();

    Optional<User> findByIdAndStatusAndDelete(Long labId, boolean status, boolean delete);

    List<User> findAll(Specification<User> specification, Sort sort);

    Optional<User> findByIdAndStatusAndDeleteAndUserRoleNot(Long labUserId, boolean status, boolean delete,UserRole userRole);

    Optional<User> findByUserRole(UserRole userRole);

    Optional<User> findByIdAndUserRole(Long id, UserRole userRole);

    Optional<User> findByMobileAndIdNot(String mobile, Long labId);
}
