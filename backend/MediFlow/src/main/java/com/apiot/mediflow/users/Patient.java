package com.apiot.mediflow.users;

import com.apiot.mediflow.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String pesel;
    private LocalDate birthDate;
    private long phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
