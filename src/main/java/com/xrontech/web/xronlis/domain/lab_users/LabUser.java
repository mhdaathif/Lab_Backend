package com.xrontech.web.xronlis.domain.lab_users;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "\"LabUser\"")
public class LabUser {
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

//    @Column(name = "user_id")
//    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private User user;

//    @Column(name = "lab_id")
//    private Long labId;

    @ManyToOne
    @JoinColumn(name = "lab_id",referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Lab lab;

}
