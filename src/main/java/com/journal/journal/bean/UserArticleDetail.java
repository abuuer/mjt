/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author anoir
 */
@Entity
public class UserArticleDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private String userFunction;
    private int mainAuthorCheck; // 1: main author / 0: co-author / -1: reviewer
    private String reviewerDecision;
    @ManyToOne
    private User user;
    
    @ManyToOne
    private Article article;

    public UserArticleDetail() {
    }

    public UserArticleDetail(String userFunction, User user, Article article, int mainAuthorCheck) {
        this.userFunction = userFunction;
        this.user = user;
        this.article = article;
        this.mainAuthorCheck = mainAuthorCheck;
    }

    public String getReviewerDecision() {
        return reviewerDecision;
    }

    public void setReviewerDecision(String reviewerDecision) {
        this.reviewerDecision = reviewerDecision;
    }

    public String getUserFunction() {
        return userFunction;
    }

    public void setUserFunction(String userFunction) {
        this.userFunction = userFunction;
    }

    public int getMainAuthorCheck() {
        return mainAuthorCheck;
    }

    public void setMainAuthorCheck(int mainAuthorCheck) {
        this.mainAuthorCheck = mainAuthorCheck;
    }

    public String getFunction() {
        return userFunction;
    }

    public void setFunction(String userFunction) {
        this.userFunction = userFunction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserArticleDetail)) {
            return false;
        }
        UserArticleDetail other = (UserArticleDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.journal.journal.bean.UserArticleDetail[ id=" + id + " ]";
    }

}
