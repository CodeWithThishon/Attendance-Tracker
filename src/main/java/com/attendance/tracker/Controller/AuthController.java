package com.attendance.tracker.Controller;

import com.attendance.tracker.Entity.User;
import com.attendance.tracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public User getLoggedInUser(Authentication authentication) {

        if (authentication == null) {
            throw new RuntimeException("Not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // VERY IMPORTANT – prevent password from going to frontend
        user.setPassword(null);

        return user;
    }
}
