/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.dao;

import com.journal.journal.bean.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 *
 * @author anoir
 */
@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    
    FileInfo findByReference(String reference);
    
    FileInfo findByUrl(String url);
    
    ResponseEntity<?> deleteByUrl(String url);
    
    @Query(value = "SELECT COUNT(*) FROM file_info WHERE article = ? && type ='review'", nativeQuery = true)
    int countReviews(int articleId);
    
    
}
