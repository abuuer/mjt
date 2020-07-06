/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.util.email.service;

import com.journal.journal.bean.UserArticleDetail;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 *
 * @author anoir
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    private void sendHtmlMessage(String to, String htmlBody) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("no-reply@mjt.uca.ma");
        helper.setTo(to);
        helper.setSubject("Final decision");
        helper.setText(htmlBody, true);
        emailSender.send(message);

    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String pseudo, String email, String lastName, String token)
            throws MessagingException {

        String confirmationUrl = "http://localhost:8080/confirm?token=" + token;
        Context thymeleafContext = new Context();
        Map model = new HashMap();
        model.put("confirmationUrl", confirmationUrl);
        model.put("lastName", lastName);
        model.put("prefix", pseudo);
        model.put("email", email);
        model.put("location", "B.P 549, Av.Abdelkarim Elkhattabi, Guéliz Marrakech");
        model.put("website", "https://fstg.heroku.com");

        thymeleafContext.setVariables(model);
        String htmlBody = thymeleafTemplateEngine.process("mail", thymeleafContext);

        sendHtmlMessage(email, htmlBody);
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendDecisionEmail(UserArticleDetail userArticleDetail)
            throws IOException, MessagingException {
        Context thymeleafContext = new Context();
        Map model = new HashMap();
        model.put("firstName", userArticleDetail.getUser().getFirstName());
        model.put("lastName", userArticleDetail.getUser().getLastName());
        model.put("prefix", userArticleDetail.getUser().getPseudo());
        model.put("email", userArticleDetail.getUser().getEmail());
        model.put("title", userArticleDetail.getArticle().getTitle());
        model.put("type", userArticleDetail.getArticle().getType());
        model.put("subDate", userArticleDetail.getArticle().getSubmitDate());
        model.put("decision", userArticleDetail.getArticle().getDecision());
        model.put("location", "B.P 549, Av.Abdelkarim Elkhattabi, Guéliz Marrakech");
        model.put("website", "https://mjt.ma");

        thymeleafContext.setVariables(model);
        String htmlBody = thymeleafTemplateEngine.process("finalDecisionMail", thymeleafContext);

        sendHtmlMessage(userArticleDetail.getUser().getEmail(), htmlBody);
    }
}
