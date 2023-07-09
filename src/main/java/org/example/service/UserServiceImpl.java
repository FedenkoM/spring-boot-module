package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PasswordDTO;
import org.example.dto.SignupDTO;
import org.example.model.Role;
import org.example.model.User;
import org.example.model.VerificationToken;
import org.example.repository.UserRepository;
import org.example.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String VALID_MSG = "valid";
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserDetailsManager userDetailsManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        var verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public User registerUser(SignupDTO userModel) {
        var user = new User();
        user.setEmail(userModel.email());
        user.setFirstName(userModel.firstName());
        user.setLastName(userModel.lastName());
        user.setPassword(userModel.password());
        userDetailsManager.createUser(user);
        return user;
    }

    @Override
    public String validateVerificationToken(String token) {
        var verificationToken = verificationTokenRepository.findByToken(token).orElseThrow();

        var user = verificationToken.getUser();
        var calendar = Calendar.getInstance();

        if (isVerificationTokenExpired(verificationToken, calendar)) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        userDetailsManager.updateUser(user);
        return VALID_MSG;
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        var verificationToken = verificationTokenRepository.findByToken(oldToken).orElseThrow();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        var passwordResetToken = new VerificationToken(user,token);
        verificationTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        var optionalVerificationToken = verificationTokenRepository.findByToken(token);

        if (optionalVerificationToken.isEmpty()) {
            return "invalid";
        }
        var passwordResetToken = optionalVerificationToken.get();

        Calendar calendar = Calendar.getInstance();

        if (isVerificationTokenExpired(passwordResetToken, calendar)) {
            verificationTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        return VALID_MSG;
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        var optionalPasswordResetToken = verificationTokenRepository
                .findByToken(token)
                .orElseThrow();
        return Optional.ofNullable(optionalPasswordResetToken.getUser());
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public String saveNewPassword(String token, PasswordDTO passwordDTO) {
        String result = validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase(VALID_MSG)) {
            return "Invalid Token";
        }
        Optional<User> user = getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            changePassword(user.get(), passwordDTO.newPassword());
            return "Password Reset Successfully";
        } else {
            return "Invalid Token";
        }
    }

    @Override
    public void changePassword(User user, String newPassword) {
        var encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userDetailsManager.updateUser(user);
    }

    private static boolean isVerificationTokenExpired(VerificationToken verificationToken, Calendar cal) {
        return (verificationToken.getExpirationTime().getTime() - cal.getTime().getTime()) <= 0;
    }

}
