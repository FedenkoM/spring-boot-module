package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PasswordDTO;
import org.example.dto.SignupDTO;
import org.example.event.RegistrationCompleteEvent;
import org.example.exception.NotFoundException;
import org.example.model.User;
import org.example.service.RegistrationService;
import org.example.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final RegistrationService registrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody @Valid SignupDTO userModel, final HttpServletRequest request) {
        var optionalUser = userService.findByEmail(userModel.email());
        var user = optionalUser.orElseGet(() -> userService.registerUser(userModel));
        if (user.isEnabled()) {
            return String.format("User with email: [%s] already registered in application", user.getEmail());
        }
        publisher.publishEvent(new RegistrationCompleteEvent(user, registrationService.applicationUrl(request)
        ));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User Verified Successfully";
        }
        return "Bad User";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          HttpServletRequest request) {
        var verificationToken = userService.generateNewVerificationToken(oldToken);
        var user = verificationToken.getUser();
        registrationService.resendVerificationTokenMail(user, registrationService.applicationUrl(request), verificationToken);
        return "Verification Link Sent";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request) {
        var user = userService.findByEmail(passwordDTO.email());
        String url = "";
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user.get(), token);
            url = registrationService.passwordResetTokenMail(user.get(), registrationService.applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordDTO passwordDTO) {
        log.info("New password was saved.");
        return userService.saveNewPassword(token, passwordDTO);
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordDTO passwordDTO) {
        User user = userService.findByEmail(passwordDTO.email())
                .orElseThrow(() ->
                        new NotFoundException("User not found with email: " + passwordDTO.email()));
        if (!userService.checkIfValidOldPassword(user, passwordDTO.oldPassword())) {
            return "Invalid Old Password";
        }
        //Save New Password
        userService.changePassword(user, passwordDTO.newPassword());
        return "Password Changed Successfully";
    }

}