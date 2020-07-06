/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.facade;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.UserArticleDetail;
import com.journal.journal.service.facade.UserArticleDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anoir
 */
@Api("This end point is responsible of managing common services between the user and the article")
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/user-article")
public class UserArticleDetailRest {
    
    
    @Autowired
    private UserArticleDetailService userArticleDetailService;

    @ApiOperation("This method returns all the articles of a chosen reviewer")
    @GetMapping("/findAllArticlesByReviewer/email/{email}")
    public List<Article> findAllArticlesByReviewer(@PathVariable String email) {
        return userArticleDetailService.findAllArticlesByReviewer(email);
    }

    @ApiOperation("This method returns all the articles of a chosen author")
    @GetMapping("/findAllArticlesByAuthor/email/{email}")
    public List<Article> findAllArticlesByAuthor(@PathVariable String email) {
        return userArticleDetailService.findAllArticlesByAuthor(email);
    }

    @ApiOperation("This method deletes a userArticleDetail object defined by user email and article ref")
    @DeleteMapping("/deleteByUserId/email/{email}/articleRef/{reference}")
    public ResponseEntity<?> deleteByUser_EmailAndArticle_Reference(@PathVariable String email,@PathVariable String reference) {
        return userArticleDetailService.deleteByUser_EmailAndArticle_Reference(email, reference);
    }

    @ApiOperation("This method updates the decision of a reviewer on an article")
    @PutMapping("/updateDecision/email/{email}/articleRef/{reference}/decision/"
            + "{decision}/additionalNotes/{additionalNotes}")
    public ResponseEntity<?> updateDecision(@PathVariable String email,@PathVariable String reference
            ,@PathVariable String decision,@PathVariable String additionalNotes) {
        return userArticleDetailService.updateDecision(email, reference, decision, additionalNotes);
    }

    
}
