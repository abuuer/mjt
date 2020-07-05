/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.Issue;
import com.journal.journal.bean.Published;
import com.journal.journal.bean.UserArticleDetail;
import com.journal.journal.bean.Volume;
import com.journal.journal.dao.VolumeRepository;
import com.journal.journal.service.util.message.ResponseMessage;
import com.journal.journal.service.facade.IssueService;
import com.journal.journal.service.facade.VolumeService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class VolumeServiceImpl implements VolumeService {

    @Autowired
    private VolumeRepository volumeRepository;

    @Autowired
    private IssueService issueService;

    @Override
    public ResponseEntity<?> save(Volume volume) {
        Volume fVolume = volumeRepository.findByNumber(volume.getNumber());
        if (fVolume != null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("volume "
                    + fVolume.getNumber() + " already exist"));
        } else {
            Volume nvolume = new Volume(volume.getNumber(), volume.getYear());
            volumeRepository.save(nvolume);
            return ResponseEntity.ok(new ResponseMessage("Volume number : " + volume.getNumber()
                    + ", Year : " + volume.getYear() + " has been added"));
        }
    }

    @Override
    public Volume findByNumber(int number) {
        Volume v = volumeRepository.findByNumber(number);
        v.getIssues().forEach((issue) -> {
            issue.setVolume(null);
            for (Published p : issue.getPublisheds()) {
                        for (UserArticleDetail urd : p.getArticle().getUserArticleDetails()) {
                            urd.setArticle(null);
                            urd.getUser().setUserArticleDetails(null);
                        }
                    }
        });
        return v;
    }

    @Override
    public List<Volume> findAll() {
        List<Volume> v = volumeRepository.findAll();
        v.forEach((volume) -> {
            volume.getIssues().forEach((issue) -> {
                issue.setVolume(null);
                for (Published p : issue.getPublisheds()) {
                        for (UserArticleDetail urd : p.getArticle().getUserArticleDetails()) {
                            urd.setArticle(null);
                            urd.getUser().setUserArticleDetails(null);
                        }
                    }
            });
        });
        return v;
    }

    @Override
    public List<Volume> findAllPublished() {
        List<Volume> volumes = volumeRepository.findAll();
        List<Volume> newVolumes = new ArrayList<>();

        volumes.forEach((volume) -> {
            List<Issue> newIssues = issueService.findAllPublishedByVol(volume.getId().intValue());
            if (!newIssues.isEmpty()) {
                volume.setIssues(newIssues);
                volume.getIssues().forEach((issue) -> {
                    issue.setVolume(null);
                    for (Published p : issue.getPublisheds()) {
                        for (UserArticleDetail urd : p.getArticle().getUserArticleDetails()) {
                            urd.setArticle(null);
                            urd.getUser().setUserArticleDetails(null);
                        }
                    }
                });
                newVolumes.add(volume);
            }
        });
        return newVolumes;
    }
}
