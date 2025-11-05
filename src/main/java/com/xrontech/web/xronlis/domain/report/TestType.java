package com.xrontech.web.xronlis.domain.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xrontech.web.xronlis.domain.lab.Lab;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "\"TestType\"")
public class TestType {
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

    @Column(name = "price")
    private double price;

    @Column(name = "lab_id")
    private Long labId;

    @ManyToOne
    @JoinColumn(name = "lab_id",referencedColumnName = "id",updatable = false,insertable = false)
    private Lab lab;

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
