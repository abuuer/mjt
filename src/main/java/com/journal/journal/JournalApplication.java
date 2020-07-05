package com.journal.journal;

import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.journal.journal.service.facade.FileInfoService;

@SpringBootApplication
public class JournalApplication implements CommandLineRunner{

    @Resource
    FileInfoService fileService;

    public static void main(String[] args) {
        SpringApplication.run(JournalApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        fileService.deleteAll();
        fileService.init();
    }
}
