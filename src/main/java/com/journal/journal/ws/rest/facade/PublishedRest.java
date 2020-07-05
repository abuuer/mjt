/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.facade;

import com.journal.journal.bean.Article;
import com.journal.journal.service.facade.PublishedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anoir
 */
@Api("This end point groups the published articles")
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/published")
public class PublishedRest {

    @Autowired
    private PublishedService publishedService;

    @ApiOperation("This method create and save a published article defined by issue and article")
    @PostMapping("/addToIssue/articleRef/{articleRef}/issueNumber/{issueNumber}/volNumber/{volNumber}")
    public ResponseEntity<?> addToIssue(@PathVariable String articleRef,
            @PathVariable int issueNumber, @PathVariable int volNumber) {
        return publishedService.addToIssue(articleRef, issueNumber, volNumber);
    }

    @ApiOperation("This method deletes a chosen article from a chosen issue")
    @Transactional
    @PutMapping("/deleteArticleFromIssue/articleRef/{articleRef}")
    public ResponseEntity<?> deleteArticleFromIssue(@PathVariable String articleRef) {
        return publishedService.deleteArticleFromIssue(articleRef);
    }

    @ApiOperation("This method returns a list of articles defined by an issue")
    @GetMapping("/findByIssue_Number/issueNumber/{issueNumber}")
    public List<Article> findByIssue_Number(@PathVariable int issueNumber) {
        return publishedService.findByIssue_Number(issueNumber);
    }

    @ApiOperation("This method count the clicks on an article to keep record of the most read")
    @PutMapping("/addClick/reference/{reference}")
    public void addClick(@PathVariable String reference) {
        publishedService.addClick(reference);
    }

    @ApiOperation("This method returns the articles with the most clicks")
    @GetMapping("/findMostRead")
    public List<Article> findMostRead() {
        return publishedService.findMostRead();
    }
    

}
