/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.Tag;
import com.journal.journal.dao.TagRepository;
import com.journal.journal.service.facade.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class TagServiceImpl implements TagService{

    @Autowired
    private TagRepository tagRepository;
    

    @Override
    public int save(Tag tag) {
        Tag fTag = tagRepository.findByName(tag.getName());
        
        if(fTag != null){
            return -1 ;
        } else {
            tagRepository.save(tag);
            return 1;
        }
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }
    
}
