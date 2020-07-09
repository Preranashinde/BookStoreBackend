package com.bridgelabz.bookstoreapp.controller;

import com.bridgelabz.bookstoreapp.payload.request.LoginRequest;
import com.bridgelabz.bookstoreapp.payload.request.SignupRequest;
import com.bridgelabz.bookstoreapp.service.AuthenticateUserServiceImpl;
import com.bridgelabz.bookstoreapp.service.IAuthenticateUserService;
import org.modelmapper.internal.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private IAuthenticateUserService iAuthenticateUserService;

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return iAuthenticateUserService.logInUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return iAuthenticateUserService.registerUser(signUpRequest);
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity forgotPassword(@RequestBody String email) {
        System.out.println(email);
        return new ResponseEntity(iAuthenticateUserService.forgotPassword(email), HttpStatus.OK);
    }

    @PutMapping("/resetpassword")
    public ResponseEntity resetPassword(@RequestBody String newPassword, @RequestHeader String token) {
        return new ResponseEntity(iAuthenticateUserService.resetPassword(newPassword, token), HttpStatus.OK);
    }

    @GetMapping("emaillink/resetpassword/{token}")
    public ResponseEntity  validateToken(@PathVariable String token) {
        return new ResponseEntity(iAuthenticateUserService.isTokenValid(token), HttpStatus.OK);
    }
}
