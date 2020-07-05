/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.User;
import com.journal.journal.security.payload.request.LoginRequest;
import com.journal.journal.security.payload.request.SignupRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
public interface UserService {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    int save(User user);

    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    ResponseEntity<?> registerUser(SignupRequest signUpRequest);
    
    ResponseEntity<?> confirmUser(String email, String password);

    ResponseEntity<?> authorToReviewer(String email);
    
    ResponseEntity<?> confirmRegistraion(String token, String password);
    
    ResponseEntity<?> deleteAccount(String email);
    
    ResponseEntity<?> dismissReviewer(String email);
    
     ResponseEntity<?> updateStatus(String email, String status);
    
}
