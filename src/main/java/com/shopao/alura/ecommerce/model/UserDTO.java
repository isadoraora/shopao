package com.shopao.alura.ecommerce.model;

import java.util.Set;

public record UserDTO(
    String username,
    String email,
    String password,
    Set<String> roles
) {
}

