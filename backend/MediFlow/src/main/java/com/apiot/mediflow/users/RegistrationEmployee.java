package com.apiot.mediflow.users;

import com.apiot.mediflow.auth.User;
import com.apiot.mediflow.collectionPoint.CollectionPoint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String position;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "collection_point_id")
    private CollectionPoint collectionPoint;
}
