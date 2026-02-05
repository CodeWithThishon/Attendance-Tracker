package com.attendance.tracker.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "school_classes", uniqueConstraints = @UniqueConstraint(columnNames = "class_name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name", nullable = false, unique = true)
    private String className;

    @OneToMany(mappedBy = "schoolClass", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Student> students;

    @OneToOne(mappedBy = "schoolClass", fetch = FetchType.LAZY)
    @JsonIgnore
    private Teacher teacher;
}
