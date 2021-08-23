package com.uno.getinline.dto;

public record LoginRequest(
        String email,
        String password
) {
    public static LoginRequest of(
            String email,
            String password
    ) {
        return new LoginRequest(email, password);
    }
}
