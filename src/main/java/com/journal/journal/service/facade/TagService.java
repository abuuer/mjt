/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.Tag;

/**
 *
 * @author anoir
 */
public interface TagService {
    
    int save (Tag tag);
    
    Tag findByName(String name);
    
}
