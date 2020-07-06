/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.UserArticleDetail;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
public interface UserArticleDetailService {

    void save(UserArticleDetail userArticleDetail);

    List<UserArticleDetail> findByUser_Email(String email);

    List<UserArticleDetail> findByArticle_Reference(String reference);

    List<Article> findAllArticlesByReviewer(String email);

    List<Article> findAllArticlesByAuthor(String email);
    
    ResponseEntity<?> deleteByUser_EmailAndArticle_Reference(String email, String reference);
    
    void delete(UserArticleDetail userArticleDetail);
    
    int countReviewers(int articleId);
    
    ResponseEntity<?> updateDecision(String email, String reference, 
            String decision, String additionalNotes);
}
