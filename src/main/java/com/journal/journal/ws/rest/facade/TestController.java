/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.facade;

import com.journal.journal.service.util.email.service.EmailService;
import com.journal.journal.service.facade.UserSpecialtyDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/journal-api/test")
public class TestController {

    @Autowired
    private UserSpecialtyDetailService userSpecialtyDetailService;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/email/{email}")
    public List<String> findTagByUser_Email(@PathVariable String email) {
        return userSpecialtyDetailService.findTagByUser_Email(email);
    }

   
   /* @GetMapping("/to/{to}/subject/{subject}")
    public void sendMessageUsingThymeleafTemplate(@PathVariable String to,@PathVariable String subject, Map<String, Object> templateModel) throws IOException, MessagingException {
        emailService.sendMessageUsingThymeleafTemplate(to, subject, templateModel);
    }*/

    @GetMapping("/to/{to}/subject/{subject}/text/{text}")
    public void sendSimpleMessage(@PathVariable String to,@PathVariable String subject,@PathVariable String text) {
        emailService.sendSimpleMessage(to, subject, text);
    }

    
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('AUTHOR') or hasRole('EDITOR') or hasRole('REVIEWER')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/author")
    @PreAuthorize("hasRole('AUTHOR')")
    public String authorAccess() {
        return "Author Board.";
    }

    @GetMapping("/editor")
    @PreAuthorize("hasRole('EDITOR')")
    public String editorAccess() {
        return "Editor Board.";
    }

    @GetMapping("/reviewer")
    @PreAuthorize("hasRole('REVIEWER')")
    public String reviewerAccess() {
        return "REVIEWER Board.";
    }
}
