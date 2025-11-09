package com.xrontech.web.xronlis.domain.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xrontech.web.xronlis.domain.lab.Lab;
import com.xrontech.web.xronlis.domain.model.BaseEntity;
import com.xrontech.web.xronlis.domain.patient.Patient;
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
@Table(name = "\"Report\"")
public class Report extends BaseEntity {
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

    @Column(name = "reference_no", nullable = false)
    private String referenceNo;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "age")
    private int age;

    @Column(name = "price")
    private double price;

    @Column(name = "lab_id")
    private Long labId;

    @OneToOne
    @JoinColumn(name = "lab_id",referencedColumnName = "id",updatable = false,insertable = false)
    private Lab lab;

    @Column(name = "test_type_id")
    private Long testTypeId;

    @OneToOne
    @JoinColumn(name = "test_type_id",referencedColumnName = "id",updatable = false,insertable = false)
    private TestType testType;

    @Column(name = "patient_id")
    private Long patientId;

    @OneToOne
    @JoinColumn(name = "patient_id",referencedColumnName = "id",updatable = false,insertable = false)
    private Patient patient;

    @Column(name = "sent_at")
    @JsonIgnore
    private LocalDateTime sentAt;

    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",updatable = false,insertable = false)
    private User user;

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
