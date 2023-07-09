package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.model.User;

@Builder
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public static UserDTO from(User user) {
        return builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}