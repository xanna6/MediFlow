package com.apiot.mediflow.auth;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements Serializable {
    private String username;
    private String password;
}
