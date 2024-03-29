package com.shopao.alura.ecommerce.model;

import java.util.Set;

public record UserResponseDto(
    Long id,
    String username,
    String email,
    Set<String> roles

) {
}
