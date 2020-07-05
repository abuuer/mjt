/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.Published;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
public interface PublishedService {

    List<Article> findByIssue_Number(int issueNumber);

    ResponseEntity<?> addToIssue(String articleRef, int issueNumber, int volNumber);

    ResponseEntity<?> deleteArticleFromIssue(String articleRef);

    void addClick(String reference);

    ResponseEntity<?> publish(Published published);

    List<Article> findMostRead();

}
