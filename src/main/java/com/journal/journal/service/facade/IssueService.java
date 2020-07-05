/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.Issue;
import com.journal.journal.bean.Volume;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
public interface IssueService {
    
    ResponseEntity<?> save(Issue issue);
    
    Issue findByNumberAndVolume_Number(int issNumber,int volNumber);
    
    ResponseEntity<?> createNewIssue(Issue issue);
    
    List<Issue> findByVolume_Number(int volumeNumber);
    
    List<Issue> findAll();
    
    List<Issue> findAllPublished();
    
    ResponseEntity<?> publishIssue(int issNumber,int volNumber);
    
    List<Issue> findAllPublishedByVol(int volNum);
    
    Issue findLatestIssue();
 }
