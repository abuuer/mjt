/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.Volume;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
public interface VolumeService {
    
    ResponseEntity<?> save(Volume volume);
    
    Volume findByNumber(int number);
    
    List<Volume> findAll();
    
    List<Volume> findAllPublished();
    
}
