package com.paper.sword.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author: zzh
 * @description: 邮件发送端
 */
@Component
@Slf4j
public class MailClient {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    public boolean sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            sender.send(helper.getMimeMessage());
            log.info("邮件内容 ==> {}", from);
        } catch (MessagingException e) {
            log.warn("发送邮件失败 ===> {}", e.getMessage());
            return false;
        }
        
        return true;
    }
    
}
