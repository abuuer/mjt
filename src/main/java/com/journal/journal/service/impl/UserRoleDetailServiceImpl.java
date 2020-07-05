/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.ERole;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserArticleDetail;
import com.journal.journal.bean.UserRoleDetail;
import com.journal.journal.dao.UserRoleDetailRepository;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.UserRoleDetailService;
import com.journal.journal.service.facade.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class UserRoleDetailServiceImpl implements UserRoleDetailService {

    @Autowired
    private UserRoleDetailRepository userRoleDetailRepository;

    @Autowired
    private UserService userService;

    @Override
    public int save(UserRoleDetail userRoleDetail) {
        userRoleDetailRepository.save(userRoleDetail);
        return 1;
    }

    @Override
    public List<UserRoleDetail> findByRole_Name(ERole name) {
        List<UserRoleDetail> urds = userRoleDetailRepository.findByRole_Name(name);

        for (UserRoleDetail userRoleDetail : urds) {
            for (UserArticleDetail userArticleDetail : userRoleDetail.getUser().getUserArticleDetails()) {
                userArticleDetail.setUser(null);
                userArticleDetail.getArticle().setUserArticleDetails(null);
            }
        }
        return urds;
    }

    @Override
    public List<UserRoleDetail> findAllReviewers() {
        List<UserRoleDetail> urd = findByRole_Name(ERole.ROLE_REVIEWER);
        if (urd == null) {
            return null;
        } else {
            return urd;
        }

    }

    @Override
    public List<UserRoleDetail> findAllAuthors() {
        List<UserRoleDetail> urd = findByRole_Name(ERole.ROLE_AUTHOR);
        if (urd == null) {
            return null;
        } else {
            return urd;
        }
    }

    @Override
    public List<UserRoleDetail> findByUser_Email(String email) {
        List<UserRoleDetail> urds = userRoleDetailRepository.findByUser_Email(email);
        for (UserRoleDetail userRoleDetail : urds) {
            userRoleDetail.getUser().setUserArticleDetails(null);
        }
        return urds;
    }

    @Override
    public ResponseEntity<?> deleteByUser_Email(String email) {
        Optional<User> fUser = userService.findByEmail(email);
        if (!fUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User doesn't exist"));
        } else {
            userRoleDetailRepository.deleteByUser_Email(email);
            return ResponseEntity.ok(new MessageResponse(fUser.get().getFirstName() + " "
                    + fUser.get().getLastName() + " is dismissed"));
        }
    }

    @Override
    public void delete(UserRoleDetail uerRoleDetail) {
        userRoleDetailRepository.delete(uerRoleDetail);
    }

    @Override
    public UserRoleDetail findByRole_NameAndUser_Email(ERole name, String Email) {
        UserRoleDetail urd = userRoleDetailRepository.findByRole_NameAndUser_Email(name, Email);
        urd.getUser().setUserArticleDetails(null);
        return urd;
    }

}
