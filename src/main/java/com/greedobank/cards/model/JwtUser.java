package com.greedobank.cards.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class JwtUser {
    @NonNull
    private String token;
    private String type = "Bearer";

    @NonNull
    private Long id;

    @NonNull
    private String email;

    @NonNull
    private String username;

    @NonNull
    private String role;
}
