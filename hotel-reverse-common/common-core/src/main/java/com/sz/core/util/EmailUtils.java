package com.sz.core.util;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;



@Component
public class EmailUtils {

    private static JavaMailSender mailSender;

    @Autowired
    private JavaMailSender mailSenderBean; // 实例成员用于注入

    @PostConstruct
    private void initStatic() {
        mailSender = this.mailSenderBean; // 将注入的bean赋值给静态变量
    }

    /**
     * 发送简单邮件（静态方法）
     */
    public static void sendSimpleEmail(String toEmail, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2857655149@qq.com");
        message.setTo(toEmail);
        message.setSubject("验证码通知");
        message.setText("验证码：" + authCode + "（5分钟有效）");

        mailSender.send(message);
    }

    /**
     * 发送HTML格式邮件（静态方法）
     */
    public static boolean sendHtmlEmail(String toEmail, String authCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("2857655149@qq.com");
            helper.setTo(toEmail);
            helper.setSubject("BotSuch Trip 验证码：");

            // HTML邮件模板，采用居中排版、边框和圆角设计
            String htmlMsg = "<div style='max-width:600px; margin:30px auto; padding:20px; border:1px solid #e0e0e0; border-radius:8px; font-family:Arial, sans-serif; background:#f9f9f9;'>"
                    + "<h2 style='color:#333; text-align:center;'>验证码：</h2>"
                    + "<div style='text-align:center; margin:20px 0;'>"
                    + "<span style='font-size:28px; font-weight:bold; color:#e84393;'>" + authCode + "</span>"
                    + "</div>"
                    + "<p style='color:#666; font-size:14px; text-align:center;'>此验证码用于修改密码，请勿转发他人或泄露。验证码有效期为2分钟。</p>"
                    + "</div>";


            helper.setText(htmlMsg, true);

            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}