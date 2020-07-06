/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.dao;

import com.journal.journal.bean.UserArticleDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author anoir
 */
@Repository
public interface UserArticleDetailRepository extends JpaRepository<UserArticleDetail, Long> {

    List<UserArticleDetail> findByUser_Email(String email);

    List<UserArticleDetail> findByArticle_Reference(String reference);

    UserArticleDetail findByArticle_ReferenceAndUser_Email(String reference, String email);

    UserArticleDetail findBymainAuthorCheckAndArticle_Reference(int i, String ref);

    @Transactional
    void deleteByUser_EmailAndArticle_Reference(String email, String reference);

    @Query(value = "SELECT COUNT(*) FROM user_article_detail WHERE article = ? && user_function = 'Reviewer'", nativeQuery = true)
    int countReviewers(int articleId);
}
