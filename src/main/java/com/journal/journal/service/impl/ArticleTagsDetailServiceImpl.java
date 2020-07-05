/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.ArticleTagsDetail;
import com.journal.journal.dao.ArticleTagsDetailRepository;
import com.journal.journal.service.facade.ArticleTagsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class ArticleTagsDetailServiceImpl implements ArticleTagsDetailService{

    @Autowired
    private ArticleTagsDetailRepository repository;
    
    @Override
    public void save(ArticleTagsDetail articleTagsDetail) {
        repository.save(articleTagsDetail);
    }
    
}
