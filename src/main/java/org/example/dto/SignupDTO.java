package org.example.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record SignupDTO(@NotNull String firstName,
                        @NotNull String lastName,
                        @Email String email,
                        @NotNull String password,
                        @NotNull String matchingPassword) {

}