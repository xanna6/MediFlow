package com.apiot.mediflow.users;

import com.apiot.mediflow.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
