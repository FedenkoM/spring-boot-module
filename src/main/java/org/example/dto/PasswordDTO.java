package org.example.dto;

public record PasswordDTO(String email,
                          String oldPassword,
                          String newPassword) {
}