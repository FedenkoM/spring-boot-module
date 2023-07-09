package org.example.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.RegistrationCompleteEvent;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    private static final String VERIFICATION_URL = "%s/api/v1/register/verifyRegistration?token=%s";
    private final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create the Verification Token for the User with Link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token,user);
        //Send Mail to user
        String url = String.format(VERIFICATION_URL, event.getApplicationUrl(), token);

        //TODO: implement sendVerificationEmail(). Temporary printing verification link to console
        log.info("Click the link to verify your account: {}", url);
    }
}