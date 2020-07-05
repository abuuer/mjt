/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.dao;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.Published;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author anoir
 */
@Repository
public interface PublishedRepositoy extends JpaRepository<Published, Long>{
    
    List<Published> findByIssue_Number(int issueNumber);
    
    Published findByArticle_Reference(String reference);
    
    @Query(value = "SELECT * FROM published ORDER BY clicks DESC LIMIT 3", nativeQuery = true)
    List<Published> findMostRead();
}
