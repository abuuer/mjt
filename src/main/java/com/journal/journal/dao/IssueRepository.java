/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.dao;

import com.journal.journal.bean.Issue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author anoir
 */
@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>{
    
    Issue findByNumberAndVolume_Number(int issNumber,int volNumber);
    
    List<Issue> findByVolume_Number(int volumeNumber);
    
    @Query(value = "SELECT * FROM issue WHERE status != 'on hold'", nativeQuery = true)
    List<Issue> findAllPublished();
    
    @Query(value = "SELECT * FROM issue WHERE status != 'on hold' AND volume = ?", nativeQuery = true)
    List<Issue> findAllPublishedByVol(int volNum);
    
    @Query(value = "SELECT * FROM issue WHERE status = 'published' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Issue findLatestIssue();
}
