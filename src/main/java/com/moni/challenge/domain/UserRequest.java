package com.moni.challenge.domain;

import lombok.Data;


@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String dob;
    private String phoneNumber;
}
