/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.facade;

import com.journal.journal.bean.Issue;
import com.journal.journal.bean.Volume;
import com.journal.journal.service.facade.IssueService;
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
@Api("This end point manages issues")
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/issue")
public class IssueRest {

    @Autowired
    private IssueService issueService;

    @ApiOperation("This method create and save a new issue to DB")
    @PostMapping("/createNewIssue")
    public ResponseEntity<?> createNewIssue(@RequestBody Issue issue) {
        return issueService.createNewIssue(issue);
    }

    @ApiOperation("This method finds all issues contained in the chosen volume")
    @GetMapping("/findByVolume_Number/volumeNumber/{volumeNumber}")
    public List<Issue> findByVolume_Number(@PathVariable int volumeNumber) {
        return issueService.findByVolume_Number(volumeNumber);
    }

    @ApiOperation("This method finds all issues in the DB")
    @GetMapping("/findAll")
    public List<Issue> findAll() {
        return issueService.findAll();
    }

    @ApiOperation("This method finds an issue defined by its number and the volume where it belongs")
    @GetMapping("/findByNumberAndVolume/issNumber/{issNumber}/volNumber/{volNumber}")
    public Issue findByNumberAndVolume_Number(@PathVariable int issNumber, @PathVariable int volNumber) {
        return issueService.findByNumberAndVolume_Number(issNumber, volNumber);
    }

    @ApiOperation("This method find all the issue with the status 'Published'")
    @GetMapping("/findAllPublished")
    public List<Issue> findAllPublished() {
        return issueService.findAllPublished();
    }

    @ApiOperation("This method update the status and the publishDate of ht chosen issue")
    @PutMapping("/publishIssue/issNumber/{issNumber}/volNumber/{volNumber}")
    public ResponseEntity<?> publishIssue(@PathVariable int issNumber, @PathVariable int volNumber) {
        return issueService.publishIssue(issNumber, volNumber);
    }

    @ApiOperation("This method finds all published issue per volume")
    @GetMapping("/findAllPublishedByVol/volNum/{volNum}")
    public List<Issue> findAllPublishedByVol(@PathVariable int volNum) {
        return issueService.findAllPublishedByVol(volNum);
    }

    @ApiOperation("This method returns latest issue by date published")
    @GetMapping("/findLatestIssue")
    public Issue findLatestIssue() {
        return issueService.findLatestIssue();
    }

}
