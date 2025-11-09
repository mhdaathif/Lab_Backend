package com.xrontech.web.xronlis.domain.patient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xrontech.web.xronlis.domain.lab.Lab;
import com.xrontech.web.xronlis.domain.model.BaseEntity;
import com.xrontech.web.xronlis.domain.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "\"Patient\"")
public class Patient extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "otp", columnDefinition = "TEXT")
    @JsonIgnore
    private String otp;

    @Column(name = "otp_expriry", columnDefinition = "TEXT")
    @JsonIgnore
    private LocalDateTime otpExpiry;

    @Column(name = "lab_id")
    private Long labId;

    @OneToOne
    @JoinColumn(name = "lab_id",referencedColumnName = "id",updatable = false,insertable = false)
    private Lab lab;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "status", nullable = false)
    @JsonIgnore
    private Boolean status = true;

    @Column(name = "delete", nullable = false)
    @JsonIgnore
    private Boolean delete = false;

    @Column(name = "deleted_at")
    @JsonIgnore
    private LocalDateTime deletedAt;

}
