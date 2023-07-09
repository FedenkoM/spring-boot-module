package org.example.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.model.VerificationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@NoArgsConstructor
@Slf4j
public class RegistrationService {

    public String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "register/savePassword?token=" + token;
        //sendVerificationEmail()
        log.info("Click the link to Reset your Password: {}", url);
        return url;
    }

    public void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/register/verifyRegistration?token=" + verificationToken.getToken();
        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}",
            url);
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
