/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.ArticleTagsDetail;
import com.journal.journal.bean.FileInfo;
import com.journal.journal.bean.Issue;
import com.journal.journal.bean.Tag;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserArticleDetail;
import com.journal.journal.bean.UserSpecialtyDetail;
import com.journal.journal.dao.ArticleRepository;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.ArticleService;
import com.journal.journal.service.facade.ArticleTagsDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.journal.journal.service.facade.FileInfoService;
import com.journal.journal.service.facade.IssueService;
import com.journal.journal.service.facade.TagService;
import com.journal.journal.service.facade.UserArticleDetailService;
import com.journal.journal.service.facade.UserService;
import com.journal.journal.service.facade.UserSpecialtyDetailService;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private FileInfoService fileService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleTagsDetailService articleTagsDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserArticleDetailService userArticleDetailService;
    @Autowired
    private UserSpecialtyDetailService userSpecialtyDetailService;
    @Autowired
    private IssueService issueService;

    @Override
    public int save(Article article) {
        Article farticle = articleRepository.findByReference(article.getReference());
        List<Tag> tags = new ArrayList<>();
        if (farticle != null) {
            return -1;
        } else {
            articleRepository.save(article);
            for (FileInfo fileInfo : article.getFileInfos()) {
                FileInfo foundedFile = fileService.findByReference(fileInfo.getReference());
                if (foundedFile == null) {
                    System.out.println("foundedFile == null");
                } else {
                    foundedFile.setArticle(article);
                    fileService.save(foundedFile);
                }
            }
            for (ArticleTagsDetail articleTagsDetail : article.getArticleTags()) {
                Tag tag = tagService.findByName(articleTagsDetail.getTag().getName());
                if (tag == null) {
                    tagService.save(articleTagsDetail.getTag());
                    tags.add(articleTagsDetail.getTag());
                    ArticleTagsDetail newAT = new ArticleTagsDetail(article, articleTagsDetail.getTag());
                    articleTagsDetailService.save(newAT);
                } else {
                    tags.add(tag);
                    ArticleTagsDetail newAT = new ArticleTagsDetail(article, tag);
                    articleTagsDetailService.save(newAT);
                }
            }
            if (article.getUserArticleDetails() != null) {
                int check = 0;
                for (UserArticleDetail userArticleDetail : article.getUserArticleDetails()) {
                    Optional<User> fUser = userService.findByEmail(userArticleDetail.getUser().getEmail());
                    // save user speacialty from article submission
                    if (fUser.get().getPassword() != null) {
                        check = 1;
                    } else {
                        check = 0;
                    }
                    if (fUser.isPresent()) {
                        UserArticleDetail uad = new UserArticleDetail("Author", fUser.get(), article, check);
                        userArticleDetailService.save(uad);
                        List<String> tagNames = userSpecialtyDetailService.findTagByUser_Email(fUser.get().getEmail());
                        for (Tag tag : tags) {
                            if (!tagNames.contains(tag.getName())) {
                                UserSpecialtyDetail usd = new UserSpecialtyDetail(fUser.get(), tag);
                                userSpecialtyDetailService.save(usd);
                            }
                        }
                    } else {
                        userService.save(userArticleDetail.getUser());
                        for (Tag tag : tags) {
                            UserSpecialtyDetail usd = new UserSpecialtyDetail(userArticleDetail.getUser(), tag);
                            userSpecialtyDetailService.save(usd);
                        }
                        UserArticleDetail uad = new UserArticleDetail("Author", userArticleDetail.getUser(), article, check);
                        userArticleDetailService.save(uad);
                        userService.save(userArticleDetail.getUser());
                    }
                }
            } else {
                return -3;
            }
            return 1;
        }
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = articleRepository.findAll();
        articles.forEach((article) -> {
            for (UserArticleDetail userArticleDetail : article.getUserArticleDetails()) {
                userArticleDetail.setArticle(null);
                userArticleDetail.getUser().setUserArticleDetails(null);
            }
        });
        return articles;
    }

    @Override
    public ResponseEntity<?> assignReviewer(String articleRef, String email) {
        Article fArticle = articleRepository.findByReference(articleRef);
        List<UserArticleDetail> userArticles = userArticleDetailService.findByArticle_Reference(articleRef);
        List<UserArticleDetail> userArticlesRelatedToReviewer = userArticleDetailService.findByUser_Email(email);
        List<User> freviewers = new ArrayList<>();
        List<UserArticleDetail> revUad = new ArrayList<>();
        userArticles.forEach((userArticle) -> {
            freviewers.add(userArticle.getUser());
        });
        for (UserArticleDetail userArticleDetail : userArticlesRelatedToReviewer) {
            if (userArticleDetail.getFunction().toLowerCase().equals("reviewer")) {
                revUad.add(userArticleDetail);
            }
        }
        Optional<User> freviewer = userService.findByEmail(email);
        if (!freviewer.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Reviewer doesn't exist"));
        } else if (freviewers.contains(freviewer.get())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(freviewer.get().getLastName()
                            + " is already assigned as a reviewer to this article"));
        } else if (fArticle == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Article doesn't exist"));
        } else {
            UserArticleDetail uad = new UserArticleDetail("Reviewer", freviewer.get(), fArticle, -1);
            fArticle.setStatus("being reviewed");
            userArticleDetailService.save(uad);
            if (revUad.size() > 5) {
                freviewer.get().setAvailability("busy");
            }
            return ResponseEntity.ok(new MessageResponse("Assigned " + freviewer.get().getFirstName() + " "
                    + freviewer.get().getLastName() + " to the article"));
        }
    }

    @Override
    public ResponseEntity<?> dismissReviewer(String articleRef, String email) {
        return ResponseEntity.ok(new MessageResponse("Assigned "));
    }

    @Override
    public Article findByReference(String referenece) {
        Article a = articleRepository.findByReference(referenece);
        for (UserArticleDetail uad : a.getUserArticleDetails()) {
            uad.setArticle(null);
            uad.getUser().setUserArticleDetails(null);
        }
        return a;
    }

    @Override
    public ResponseEntity<?> updateStatus(String articleRef, String status, String decision) {
        Article farticle = articleRepository.findByReference(articleRef);
        if (farticle == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Article doesn't exist"));
        } else {
            farticle.setStatus(status);
            farticle.setDecision(decision);
            if (status.toLowerCase().equals("accepted")) {
                farticle.setAcceptDate(new Date());
            }
            articleRepository.save(farticle);
            return ResponseEntity.ok(new MessageResponse("Status updated to " + status));
        }
    }

    @Override
    public List<Article> findByStatus(String status) {
        List<Article> articles = articleRepository.findByStatus(status);
        articles.forEach((article) -> {
            for (UserArticleDetail userArticleDetail : article.getUserArticleDetails()) {
                userArticleDetail.setArticle(null);
                userArticleDetail.getUser().setUserArticleDetails(null);
            }
        });
        return articles;
    }

}
