/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.ERole;
import com.journal.journal.bean.UserRoleDetail;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
public interface UserRoleDetailService {

    int save(UserRoleDetail userRoleDetail);

    List<UserRoleDetail> findByUser_Email(String email);

    List<UserRoleDetail> findByRole_Name(ERole name);

    List<UserRoleDetail> findAllReviewers();

    List<UserRoleDetail> findAllAuthors();

    ResponseEntity<?> deleteByUser_Email(String email);

    void delete(UserRoleDetail uerRoleDetail);

    UserRoleDetail findByRole_NameAndUser_Email(ERole name, String Email);

}
