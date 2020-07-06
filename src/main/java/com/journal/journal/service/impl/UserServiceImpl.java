/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.AccountActivation;
import com.journal.journal.bean.ERole;
import com.journal.journal.bean.Role;
import com.journal.journal.bean.Tag;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserDetailsImpl;
import com.journal.journal.bean.UserRoleDetail;
import com.journal.journal.bean.UserSpecialtyDetail;
import com.journal.journal.dao.AccountActivationRepository;
import com.journal.journal.dao.UserRepository;
import com.journal.journal.service.util.email.service.EmailService;
import com.journal.journal.service.util.message.ResponseMessage;
import com.journal.journal.security.jwt.JwtUtils;
import com.journal.journal.security.payload.request.LoginRequest;
import com.journal.journal.security.payload.request.SignupRequest;
import com.journal.journal.security.payload.response.JwtResponse;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.RoleService;
import com.journal.journal.service.facade.TagService;
import com.journal.journal.service.facade.UserArticleDetailService;
import com.journal.journal.service.facade.UserRoleDetailService;
import com.journal.journal.service.facade.UserService;
import com.journal.journal.service.facade.UserSpecialtyDetailService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleDetailService userRoleDetailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserSpecialtyDetailService userSpecialtyDetailService;

    @Autowired
    private TagService tagService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AccountActivationRepository accountActivation;

    @Autowired
    private UserArticleDetailService userArticleDetailService;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setUserArticleDetails(null);
        }
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return UserDetailsImpl.build(user);
    }

    @Override
    public int save(User user) {
        userRepository.save(user);
        return 1;
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getEmail(),
                    roles));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(("Invalid email or password")));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(("Authentication failed")));
        }

    }

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        Optional<User> fUser = findByEmail(signUpRequest.getEmail());
        if (fUser.isPresent() && fUser.get().getPassword() != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("-1"));
        }
        // Create new user's account
        User user = new User(signUpRequest.getPseudo(), signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getMiddleName(), signUpRequest.getEmail(), signUpRequest.getDegree(), signUpRequest.getAdress(),
                signUpRequest.getCountry(), signUpRequest.getRegion(), signUpRequest.getCity(), signUpRequest.getPostalCode(),
                signUpRequest.getPhone(), signUpRequest.getFax(), signUpRequest.getInstitution(), signUpRequest.getDepartement(),
                signUpRequest.getInstAdress(), signUpRequest.getInstPhone());

        List<String> strRoles = signUpRequest.getRole();
        List<Role> roles = new ArrayList<>();

        if (strRoles == null) {
            Role authorRole = roleService.findByName(ERole.ROLE_AUTHOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Role userRole = roleService.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(authorRole);
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "author":
                        Role authorRole = roleService.findByName(ERole.ROLE_AUTHOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(authorRole);

                        break;
                    case "editor":
                        Role editorRole = roleService.findByName(ERole.ROLE_EDITOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(editorRole);

                        break;
                    case "reviewer":
                        Role reviewerRole = roleService.findByName(ERole.ROLE_REVIEWER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(reviewerRole);
                        break;
                    default:
                        Role userRole = roleService.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        Role authorRoleDef = roleService.findByName(ERole.ROLE_AUTHOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                        roles.add(authorRoleDef);
                }
            });
        }

        if (fUser.isPresent() && fUser.get().getPassword() == null) {

            fUser.get().setFirstName(user.getFirstName());
            fUser.get().setLastName(user.getLastName());
            fUser.get().setMiddleName(user.getMiddleName());
            fUser.get().setDegree(user.getDegree());
            fUser.get().setAdress(user.getAdress());
            fUser.get().setCountry(user.getCountry());
            fUser.get().setRegion(user.getRegion());
            fUser.get().setCity(user.getCity());
            fUser.get().setPostalCode(user.getPostalCode());
            fUser.get().setPhone(user.getPhone());
            fUser.get().setFax(user.getFax());
            fUser.get().setInstitution(user.getInstitution());
            fUser.get().setDepartement(user.getDepartement());
            fUser.get().setInstAdress(user.getInstAdress());
            fUser.get().setInstPhone(user.getInstPhone());
            save(fUser.get());
            String token = UUID.randomUUID().toString();
            AccountActivation accountAct = new AccountActivation(token, user);
            accountActivation.save(accountAct);
            try {
                emailService.sendMessageUsingThymeleafTemplate(user.getPseudo(),
                        user.getEmail(), user.getLastName(), token);
            } catch (IOException | MessagingException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            roles.stream().map((role) -> {
                UserRoleDetail userRoleDetail = new UserRoleDetail();
                userRoleDetail.setRole(role);
                return userRoleDetail;
            }).map((userRoleDetail) -> {
                userRoleDetail.setUser(fUser.get());
                return userRoleDetail;
            }).forEachOrdered((userRoleDetail) -> {
                userRoleDetailService.save(userRoleDetail);
            });
            if (signUpRequest.getSpecialty() != null) {
                for (String s : signUpRequest.getSpecialty()) {
                    Tag t = new Tag(s);
                    Tag ftag = tagService.findByName(s);
                    if (ftag == null) {
                        tagService.save(t);
                        UserSpecialtyDetail usd = new UserSpecialtyDetail(fUser.get(), t);
                        userSpecialtyDetailService.save(usd);
                    } else {
                        UserSpecialtyDetail usd = new UserSpecialtyDetail(fUser.get(), ftag);
                        userSpecialtyDetailService.save(usd);
                    }
                }
            }

        } else {
            save(user);

            String token = UUID.randomUUID().toString();
            AccountActivation accountAct = new AccountActivation(token, user);
            accountActivation.save(accountAct);
            try {
                emailService.sendMessageUsingThymeleafTemplate(user.getPseudo(),
                        user.getEmail(), user.getLastName(), token);
            } catch (IOException | MessagingException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            roles.stream().map((role) -> {
                UserRoleDetail userRoleDetail = new UserRoleDetail();
                userRoleDetail.setRole(role);
                return userRoleDetail;
            }).map((userRoleDetail) -> {
                userRoleDetail.setUser(user);
                return userRoleDetail;
            }).forEachOrdered((userRoleDetail) -> {
                userRoleDetailService.save(userRoleDetail);
            });
            if (signUpRequest.getSpecialty() != null) {
                for (String s : signUpRequest.getSpecialty()) {
                    Tag t = new Tag(s);
                    Tag ftag = tagService.findByName(s);
                    if (ftag == null) {
                        tagService.save(t);
                        UserSpecialtyDetail usd = new UserSpecialtyDetail(user, t);
                        userSpecialtyDetailService.save(usd);
                    } else {
                        UserSpecialtyDetail usd = new UserSpecialtyDetail(user, ftag);
                        userSpecialtyDetailService.save(usd);
                    }
                }
            }

        }
        return ResponseEntity.ok(new MessageResponse("1"));
    }

    @Override
    public ResponseEntity<?> authorToReviewer(String email) {
        Optional<User> fAuthor = userRepository.findByEmail(email);
        List<UserRoleDetail> userRoleDetails = userRoleDetailService.findByUser_Email(email);
        UserRoleDetail authorRole = userRoleDetailService
                .findByRole_NameAndUser_Email(ERole.ROLE_AUTHOR, email);
        List<Role> roles = new ArrayList<>();
        userRoleDetails.forEach((furd) -> {
            roles.add(furd.getRole());
        });
        if (!fAuthor.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Author doesn't exist"));
        } else {
            Role reviewerRole = roleService.findByName(ERole.ROLE_REVIEWER).get();
            UserRoleDetail urd = new UserRoleDetail(fAuthor.get(), reviewerRole);
            if (roles.contains(reviewerRole)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Author is already a reviewer"));
            } else {
                userRoleDetailService.save(urd);
                fAuthor.get().setAvailability("Available");
                userRepository.save(fAuthor.get());
                userRoleDetailService.delete(authorRole);
                return ResponseEntity.ok(new MessageResponse(fAuthor.get().getLastName()
                        + " " + fAuthor.get().getFirstName() + " is a reviewer now!"));
            }
        }
    }

    @Override
    public ResponseEntity<?> confirmUser(String email, String password) {
        Optional<User> fuser = userRepository.findByEmail(email);
        if (!fuser.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("User not found"));
        } else if (fuser.get().getPassword() != null) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Registration already confirmed"));
        } else {
            fuser.get().setPassword(password);
            userRepository.save(fuser.get());
            return ResponseEntity.ok(new ResponseMessage("Your account has been activated successfully"));
        }
    }

    @Override
    public ResponseEntity<?> confirmRegistraion(String token, String password) {
        AccountActivation accountAct = accountActivation.findByToken(token);

        Calendar cal = Calendar.getInstance();
        if (accountAct == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("The invitation link "
                    + "isn't valid. Perhaps it has been already used!"));
        }
        if ((accountAct.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Expired Token"));
        }
        User user = accountAct.getUser();
        accountActivation.delete(accountAct);
        user.setPassword(encoder.encode(password));
        user.setVerified(true);
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseMessage("Confirmed successfully"));
    }

    @Override
    public ResponseEntity<?> deleteAccount(String email) {
        Optional<User> fuser = userRepository.findByEmail(email);
        List<UserRoleDetail> fuserRoles = userRoleDetailService.findByUser_Email(email);

        if (!fuser.isPresent()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("User doesn't exist"));
        } else if (fuser.get().getPassword() == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("User already deleted"));
        } else {
            for (UserRoleDetail fuserRole : fuserRoles) {
                userRoleDetailService.delete(fuserRole);
            }
            fuser.get().setPassword(null);
            userRepository.save(fuser.get());
            return ResponseEntity.ok(new ResponseMessage("User deleted successfully"));
        }
    }

    @Override
    public ResponseEntity<?> dismissReviewer(String email) {
        Optional<User> fuser = userRepository.findByEmail(email);
        if (!fuser.isPresent()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("USER NOT FOUND"));
        } else {
            List<UserRoleDetail> fRoleDetails = userRoleDetailService.findByUser_Email(email);
            UserRoleDetail fuserRole = new UserRoleDetail();
            List<Role> roles = new ArrayList<>();
            fRoleDetails.forEach((fRoleDetail) -> {
                roles.add(fRoleDetail.getRole());
                if (fRoleDetail.getRole().getName() == ERole.ROLE_REVIEWER) {
                    fuserRole.setId(fRoleDetail.getId());
                    fuserRole.setRole(fRoleDetail.getRole());
                    fuserRole.setUser(fRoleDetail.getUser());
                }
            });
            if (!roles.contains(roleService.findByName(ERole.ROLE_REVIEWER).get())) {
                return ResponseEntity.badRequest().body(new ResponseMessage("User is not a reviewer"));
            } else {
                userRoleDetailService.delete(fuserRole);
                Role authorRole = roleService.findByName(ERole.ROLE_AUTHOR).get();
                UserRoleDetail urd = new UserRoleDetail(fuser.get(), authorRole);
                userRoleDetailService.save(urd);
                return ResponseEntity.ok(new MessageResponse(fuser.get().getLastName()
                        + " " + fuser.get().getFirstName() + " is dismissed as a reviewer"));
            }
        }

    }

    @Override
    public ResponseEntity<?> updateStatus(String email, String status) {
        Optional<User> fuser = userRepository.findByEmail(email);
        if (!fuser.isPresent()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("User is not found"));
        } else if (!status.equals("busy") || !status.equals("available") || !status.equals("not available")) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Status is not valid"));
        } else {
            fuser.get().setAvailability(status);
            userRepository.save(fuser.get());
            return ResponseEntity.ok(new MessageResponse("Updated status to " + status));
        }
    }

}
