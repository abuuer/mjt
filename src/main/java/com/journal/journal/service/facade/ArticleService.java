/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.Article;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
public interface ArticleService {

    public int save(Article article);

    public List<Article> findAll();

    public Article findByReference(String referenece);

    public ResponseEntity<?> assignReviewer(String articleRef, String email);

    public ResponseEntity<?> dismissReviewer(String articleRef, String email);

    public ResponseEntity<?> updateStatus(String articleRef, String status, String decision);

    List<Article> findByStatus(String status);

}
