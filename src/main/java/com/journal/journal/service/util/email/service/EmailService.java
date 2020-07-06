/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.util.email.service;

import com.journal.journal.bean.UserArticleDetail;
import java.io.IOException;
import javax.mail.MessagingException;

/**
 *
 * @author anoir
 */
public interface EmailService {

    void sendMessageUsingThymeleafTemplate(String pseudo, String email, String lastName,String token)
            throws IOException, MessagingException;

    public void sendDecisionEmail(UserArticleDetail userArticleDetail)
            throws IOException, MessagingException;

    public void sendSimpleMessage(String to, String subject, String text);
}
