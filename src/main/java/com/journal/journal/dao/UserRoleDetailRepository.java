/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.dao;

import com.journal.journal.bean.ERole;
import com.journal.journal.bean.UserRoleDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author anoir
 */
@Repository
public interface UserRoleDetailRepository extends JpaRepository<UserRoleDetail, Long> {

    List<UserRoleDetail> findByUser_Email(String email);

    List<UserRoleDetail> findByRole_Name(ERole name);

    UserRoleDetail findByRole_NameAndUser_Email(ERole name,String Email);

    @Transactional
    void deleteByUser_Email(String email);
}
