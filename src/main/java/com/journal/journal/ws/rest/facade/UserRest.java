/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.facade;

import com.journal.journal.bean.User;
import com.journal.journal.security.payload.request.LoginRequest;
import com.journal.journal.security.payload.request.SignupRequest;
import com.journal.journal.service.facade.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anoir
 */
@Api("This end point manages users")
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/user")
public class UserRest {

    @Autowired
    private UserService userService;

    @ApiOperation("This method checks user's crendetials and returns a jwt with roles"
            + " of the user in case of a successfull login")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @ApiOperation("This method save the crdentilas of the user entered during registration")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

    @ApiOperation("This method returns a user by chosen email")
    @GetMapping("/email/{email}")
    public Optional<User> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @ApiOperation("This method changes the role of a user from author to reviewer")
    @PutMapping("/authorToReviewer/email/{email}")
    public ResponseEntity<?> authorToReviewer(@PathVariable String email) {
        return userService.authorToReviewer(email);
    }

   /* @ApiOperation("This method activates the user's account + set the password")
    @PutMapping("/confirmUser/email/{email]/password/{password}")
    public ResponseEntity<?> confirmUser(String email, String password) {
        return userService.confirmUser(email, password);
    }*/

    @ApiOperation("This method activates the user's account + set the password")
    @PutMapping("/confirmRegistraion/token/{token}/password/{password}")
    public ResponseEntity<?> confirmRegistraion(@PathVariable String token,@PathVariable String password) {
        return userService.confirmRegistraion(token,password);
    }

    @ApiOperation("This method deletes the user's account")
    @Transactional
    @DeleteMapping("/deleteAccount/email/{email}")
    public ResponseEntity<?> deleteAccount(@PathVariable String email) {
        return userService.deleteAccount(email);
    }

    @ApiOperation("This method deletes the user's role as a reviewer to become just an author")
    @DeleteMapping("/dismissReviewer/email/{email}")
    @Transactional
    public ResponseEntity<?> dismissReviewer(@PathVariable String email) {
        return userService.dismissReviewer(email);
    }

    @ApiOperation("This method updates the reviewer's status (Available,Not Available, busy)")
    @PutMapping("/updateStatus/email/{email}/email/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable String email,@PathVariable String status) {
        return userService.updateStatus(email, status);
    }
    
    
    
    
    
}
