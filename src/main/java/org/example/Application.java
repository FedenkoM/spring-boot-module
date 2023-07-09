package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "Spring BOOT API"),
    servers = @Server(url = "${server.servlet.context-path}")
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("Hello spring boot!!!");
            var admin = new User();
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.addRole(Role.ROLE_ADMIN);
            admin.setEnabled(true);
            if (userRepository.existsByEmail(admin.getEmail())) return;
            userRepository.save(admin);
        };
    }
}
