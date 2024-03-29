package com.greedobank.cards.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
