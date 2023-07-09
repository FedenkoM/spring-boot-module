package org.example.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.dto.UserDTO;
import org.example.exception.NotFoundException;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/{id:[\\d]+}")
    @PreAuthorize("(#user.id == #id) or hasAuthority('WRITE')")
    public ResponseEntity<UserDTO> user(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                        @PathVariable Long id) {
        return ResponseEntity.ok(UserDTO.from(userRepository.findById(id).orElseThrow()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<List<UserDTO>> users() {
        var userDTOS = userRepository.findAll().stream().map(UserDTO::from).toList();
        return ResponseEntity.ok(userDTOS);
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('WRITE') AND hasAuthority('UPDATE')")
    public ResponseEntity<String> addUserRole(@RequestParam @Pattern(regexp = "[\\d]+") Long userId,
                                              @Parameter(in = ParameterIn.QUERY, name = "role",
                                                    schema = @Schema(implementation = Role.class)) @RequestParam Role role) {
        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with ID:[%d] not found", userId)));
        var isRoleExist = Arrays.asList(Role.values()).contains(role);

        if (!isRoleExist) {throw new NotFoundException("Role not exist!");}

        user.addRole(role);
        userRepository.save(user);
        return ResponseEntity.ok("User roles was successfully updated");
    }
}