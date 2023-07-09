package org.example.service;



import org.example.dto.PasswordDTO;
import org.example.dto.SignupDTO;
import org.example.model.User;
import org.example.model.VerificationToken;

import java.util.Optional;

public interface UserService {
    User registerUser(SignupDTO userModel);

    String validateVerificationToken(String token);

    void saveVerificationTokenForUser(String token, User user);

    VerificationToken generateNewVerificationToken(String oldToken);

    Optional<User> findByEmail(String email);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    boolean checkIfValidOldPassword(User user, String oldPassword);

    String saveNewPassword(String token, PasswordDTO passwordDTO);

    void changePassword(User user, String newPassword);

    void createPasswordResetTokenForUser(User user, String token);
}
