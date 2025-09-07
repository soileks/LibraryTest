package com.example.library.model;

import com.example.library.annotation.ValidAge;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;


@Entity
@Table(name = "clients", indexes = {
        @Index(name = "idx_full_name", columnList = "fullName"),
        @Index(name = "idx_birth_date", columnList = "birthDate")
})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false, length = 200)
    @Pattern(regexp = "^[\\p{L} \\-'’.]+$",
            message = "Full name can only contain letters, spaces, hyphens, and apostrophes")
    private String fullName;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ValidAge(message = "Age must be between 5 and 120 years")
    @Column(nullable = false)
    private LocalDate birthDate;


    public Client() {}

    public Client(String fullName, LocalDate birthDate) {
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
}
