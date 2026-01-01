package com.apiot.mediflow.users;

import com.apiot.mediflow.auth.User;
import com.apiot.mediflow.referral.Referral;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
    private Long phoneNumber;
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Referral> referrals;

    public Patient(Long id, String firstName, String lastName, String pesel, LocalDate birthDate, long phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public Patient(String firstName, String lastName, String pesel, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.birthDate = birthDate;
    }
}
