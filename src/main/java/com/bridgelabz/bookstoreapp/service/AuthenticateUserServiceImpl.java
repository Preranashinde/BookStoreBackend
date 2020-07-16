package com.bridgelabz.bookstoreapp.service;

import com.bridgelabz.bookstoreapp.dto.EmailDto;
import com.bridgelabz.bookstoreapp.entity.Role;
import com.bridgelabz.bookstoreapp.entity.User;
import com.bridgelabz.bookstoreapp.model.ERole;
import com.bridgelabz.bookstoreapp.payload.request.LoginRequest;
import com.bridgelabz.bookstoreapp.payload.request.SignupRequest;
import com.bridgelabz.bookstoreapp.payload.response.JwtResponse;
import com.bridgelabz.bookstoreapp.payload.response.MessageResponse;
import com.bridgelabz.bookstoreapp.repository.RoleRepository;
import com.bridgelabz.bookstoreapp.repository.UserRepository;
import com.bridgelabz.bookstoreapp.utility.JwtUtils;
import com.bridgelabz.bookstoreapp.utility.RabbitMq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:message.properties")
public class AuthenticateUserServiceImpl implements IAuthenticateUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailDto emailDto;

    @Autowired
    private RabbitMq rabbitMq;

    @Autowired
    private Environment environment;

    @Override
    public ResponseEntity logInUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

        if (user.get().isVerified()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        } else {
            return new ResponseEntity(environment.getProperty("VERIFY_ACCOUNT"), HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
        }
    }

    @Override
    public ResponseEntity registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            System.out.println(signUpRequest.getEmail());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(environment.getProperty("USERNAME_NOT_AVAILABLE")));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(environment.getProperty("EMAIL_IS_TAKEN")));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhoneNumber());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseThrow(() ->
            throw new RuntimeException(environment.getProperty("ROLE_NOT_FOUND"));
//            );
//            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(environment.getProperty("ROLE_NOT_FOUND")));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(environment.getProperty("ROLE_NOT_FOUND")));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        sendEmailToVerify(user);
        return ResponseEntity.ok(new MessageResponse(environment.getProperty("USER_REGISTERED")));
    }

    @Override
    public String forgotPassword(String email) {
        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email);
            String token = jwtUtils.jwtTokenUsingUsername(user.getUsername());
            sendEmailToResetPassword(email, token);
//            System.out.println(jwtUtils.getUserNameFromJwtToken(token));
            return environment.getProperty("EMAIL_SENT");
        }
        return environment.getProperty("EMAIL_NOT_EXISTS");
    }

    @Override
    public String resetPassword(String newPassword, String token) {
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            Optional<User> user = userRepository.findByUsername(username);
            user.get().setPassword(encoder.encode(newPassword));
            userRepository.save(user.get());
            return environment.getProperty("PASSWORD_CHANGED");
        }
        return environment.getProperty("JWT_NOT_VALID");
    }

    @Override
    public Boolean isTokenValid(String token) {
        if (jwtUtils.validateJwtToken(token))
            return true;
        return false;
    }

    private void sendEmailToResetPassword(String email, String token) {
        emailDto.setTo(email);
        emailDto.setFrom("${EMAIL}");
        emailDto.setSubject(environment.getProperty("WELCOME_HEADER"));
        emailDto.setBody("Please click this link to verify your account " + "http://localhost:3000/passwordset/" + token);
        rabbitMq.sendMessageToQueue(emailDto);
    }

    private void sendEmailToVerify(User user) throws MailException {
        emailDto.setTo(user.getEmail());
        emailDto.setFrom("${EMAIL}");
        emailDto.setSubject(environment.getProperty("WELCOME_HEADER"));
        emailDto.setBody("Please click this link to verify your account " + "http://localhost:8080/verifyaccount/" + user.getId());
        rabbitMq.sendMessageToQueue(emailDto);
    }
}
