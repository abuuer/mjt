/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.Tag;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserRoleDetail;
import com.journal.journal.bean.UserSpecialtyDetail;
import com.journal.journal.dao.UserSpecialtyDetailRepository;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.UserService;
import com.journal.journal.service.facade.UserSpecialtyDetailService;
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
public class UserSpecialtyDetailImpl implements UserSpecialtyDetailService {

    @Autowired
    private UserSpecialtyDetailRepository repository;
    @Autowired
    private UserService userService;

    @Override
    public void save(UserSpecialtyDetail userSpecialtyDetail) {
        repository.save(userSpecialtyDetail);
    }

    @Override
    public List<String> findTagByUser_Email(String email) {
        Optional<User> fuser = userService.findByEmail(email);
        if (!fuser.isPresent()) {
            return null;
        } else {
            List<UserSpecialtyDetail> usds = repository.findByUser_Email(email);
            List<Tag> tags = new ArrayList<>();
            List<String> names = new ArrayList<>();
            for (UserSpecialtyDetail usd : usds) {
                names.add(usd.getTag().getName());
            }
            return names;
        }

    }

    @Override
    public List<UserSpecialtyDetail> findByUser_Email(String email) {
        return repository.findByUser_Email(email);
    }

    @Override
    public ResponseEntity<?> deleteByUser_Email(String email) {
         Optional<User> fUser = userService.findByEmail(email);
        if (!fUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User doesn't exist"));
        } else {
            repository.deleteByUser_Email(email);
            return ResponseEntity.ok(new MessageResponse(fUser.get().getFirstName() + " "
                    + fUser.get().getLastName() + " is dismissed"));
        }
    }

    @Override
    public void delete(UserSpecialtyDetail userSpecialtyDetail) {
        repository.delete(userSpecialtyDetail);
    }

   

}
