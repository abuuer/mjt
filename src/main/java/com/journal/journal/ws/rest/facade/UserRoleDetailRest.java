/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.facade;

import com.journal.journal.bean.User;
import com.journal.journal.bean.UserRoleDetail;
import com.journal.journal.service.facade.UserRoleDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anoir
 */
@Api("This end point is responsible of managing common services between the user and the roles")
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/user-role")
public class UserRoleDetailRest {

    @Autowired
    private UserRoleDetailService userRoleDetailService;

    @ApiOperation("This method returns all the users with role 'Reviewer'")
    @GetMapping("/findAllReviewers")
    public List<UserRoleDetail> findAllReviewers() {
        return userRoleDetailService.findAllReviewers();
    }

    @ApiOperation("This method returns all the users with role 'Author'")
    @GetMapping("/findAllAuthors")
    public List<UserRoleDetail> findAllAuthors() {
        return userRoleDetailService.findAllAuthors();
    }

}
