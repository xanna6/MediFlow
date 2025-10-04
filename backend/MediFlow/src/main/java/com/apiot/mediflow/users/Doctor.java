package com.apiot.mediflow.users;

import com.apiot.mediflow.auth.User;
import com.apiot.mediflow.referral.Referral;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String specialization;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Referral> referrals;

    public Doctor(Long id, String firstname, String lastname, String specialization) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.specialization = specialization;
    }
}
