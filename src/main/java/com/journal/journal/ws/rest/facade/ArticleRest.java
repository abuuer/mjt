/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.facade;

import com.journal.journal.bean.Article;
import com.journal.journal.service.facade.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@Api("This end point allow article management ")
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/article")
public class ArticleRest {

    @Autowired
    private ArticleService articleService;

    @ApiOperation("This method save a new article")
    @PostMapping("/save")
    public int save(@RequestBody Article article) {
        return articleService.save(article);
    }

    @ApiOperation("This method fetch all the article from DB")
    @GetMapping("/all")
    public List<Article> findAll() {
        return articleService.findAll();
    }

    @ApiOperation("This method assign a reviewer to a chosen article")
    @PutMapping("/assignReviewer/articleRef/{articleRef}/email/{email}")
    public ResponseEntity<?> assignReviewer(@PathVariable String articleRef, @PathVariable String email) {
        return articleService.assignReviewer(articleRef, email);
    }

    @ApiOperation("This method allow to dismiss an assigned reviewer from reviewing an article")
    @PutMapping("/dismissReviewer/articleRef/{articleRef}/email/{email}")
    public ResponseEntity<?> dismissReviewer(String articleRef, String email) {
        return articleService.dismissReviewer(articleRef, email);
    }

    @ApiOperation("This method update the article status (Accepted - Reviewed - Rejected ...)")
    @PutMapping("/updateStatus/articleRef/{articleRef}/status/{status}/decision/{decision}")
    public ResponseEntity<?> updateStatus(@PathVariable String articleRef,
             @PathVariable String status, @PathVariable String decision) {
        return articleService.updateStatus(articleRef, status, decision);
    }

    @ApiOperation("This method finds articles by status")
    @GetMapping("/findByStatus/status/{status}")
    public List<Article> findByStatus(@PathVariable String status) {
        return articleService.findByStatus(status);
    }

    @GetMapping("/findByReference/referenece/{referenece}")
    public Article findByReference(@PathVariable String referenece) {
        return articleService.findByReference(referenece);
    }

}
