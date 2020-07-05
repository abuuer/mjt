/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.FileInfo;
import com.journal.journal.dao.FileInfoRepository;
import com.journal.journal.service.util.message.ResponseMessage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.journal.journal.bean.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.util.FileSystemUtils;
import com.journal.journal.service.facade.FileInfoService;
import com.journal.journal.ws.rest.facade.FileRest;
import java.util.Optional;
import com.journal.journal.service.facade.ArticleService;
import com.journal.journal.service.facade.UserArticleDetailService;
import java.util.Random;

/**
 *
 * @author anoir
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserArticleDetailService userArticleDetailService;

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void storeFile(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file, String fileType) {
        String message = "";
        try {
            String fileName;

            storeFile(file);
            fileName = file.getOriginalFilename();
            Resource currentfile = loadFile(file.getOriginalFilename());
            //MvcUriComponentsBuilder used tp generate url
            String url = MvcUriComponentsBuilder.fromMethodName(FileRest.class, "getFile",
                    randomeString() + "_" + currentfile.getFilename() + "_" + randomeString()).build().toString();
            FileInfo newFile = new FileInfo(currentfile.getFilename(), url, fileType);
            saveInfo(newFile);

            //message = "Uploaded the file successfully: " + fileName;
            return ResponseEntity.status(HttpStatus.OK).body(newFile);
        } catch (Exception e) {
            message = "Fail to upload files!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage(message));
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(FileRest.class, "getFile", path.getFileName().toString()).build().toString();
            FileInfo f = fileInfoRepository.findByUrl(url);
            return new FileInfo(filename, url, f.getType());
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @Override
    public ResponseEntity<Resource> getFile(String filename) {
        Resource file = loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + file.getFilename() + "\"").body(file);
    }

    @Override
    public int saveInfo(FileInfo file) {
        fileInfoRepository.save(file);
        Optional<FileInfo> f = fileInfoRepository.findById(file.getId());
        f.get().setReference("ref" + file.getId());
        fileInfoRepository.save(f.get());
        return 1;

    }

    @Override
    public void save(FileInfo file) {
        FileInfo fFile = fileInfoRepository.findByUrl(file.getUrl());
        Article farticle = articleService.findByReference(file.getArticle().getReference());
        if (fFile != null && fFile.getArticle() == null) {
            fFile.setArticle(farticle);
            fileInfoRepository.save(fFile);
        } else {
            fileInfoRepository.save(file);
        }
        boolean statusCheck = updateStatus(farticle);
        if (statusCheck == true) {
            articleService.updateStatus(farticle.getReference(), "Reviewed", null);
        }
    }

    private boolean updateStatus(Article article) {
        if (userArticleDetailService.countReviewers(article.getId().intValue()) == 0) {
            return false;
        } else return userArticleDetailService.countReviewers(article.getId().intValue())
                == countReviews(article.getId().intValue());
    }

    @Override
    public FileInfo findByReference(String reference) {
        return fileInfoRepository.findByReference(reference);
    }

    public String randomeString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    @Override
    public ResponseEntity<?> deleteByUrl(String url) {
        fileInfoRepository.deleteByUrl(url);
        return ResponseEntity.ok(new ResponseMessage("File deleted successfully"));
    }

    @Override
    public int countReviews(int articleId) {
        return fileInfoRepository.countReviews(articleId);
    }
}
