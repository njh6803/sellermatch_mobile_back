package com.sellermatch.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Component
public class MailUtil{

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String from = "sellermatch3@daum.net";

    /**
     * 메일을 발송한다.
     *
     * @param to - 수신자 메일 주소
     * @param subject  - 제목
     * @param text  - 닉네임 또는 임시비밀번호
     * @param type  - 메일유형 - accept, recommand, welcomeMail, findPw
     */
    @Async
    public void sendMail(String to, String subject, String type, String text) {

        MimeMessagePreparator message = mimeMessage -> {
            String content = bulid(type, text);

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };
        mailSender.send(message);
    }

    @Async
    public void sendMail(String to, String subject, String nickName, String type, String applyTypeName) {
        MimeMessagePreparator message = mimeMessage -> {
            String content = bulid(nickName, type, applyTypeName);

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };
        mailSender.send(message);
    }

    @Async
    public void sendMail(String to, String subject, String nickName, String type, String applyTypeName, String projTitle, String memSortName) {
        MimeMessagePreparator message = mimeMessage -> {
            String content = bulid(nickName, type, applyTypeName, projTitle, memSortName);

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };
        mailSender.send(message);
    }

    @Async
    public void sendMailReply(String to, String subject, String type, String nickName, String projTitle) {

        MimeMessagePreparator message = mimeMessage -> {
            String content = bulid(nickName, type, projTitle);

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };
        mailSender.send(message);
    }

    private String bulid(String nickName, String type, String projTitle){
        Context context = new Context();
        if (!Util.isEmpty(projTitle)) {
            context.setVariable("projTitle", projTitle);
        }
        context.setVariable("nickName", nickName);
        return templateEngine.process(type, context);
    };

    private String bulid(String nickName, String type, String applyTypeName, String projTitle, String memSortName) {
        Context context = new Context();
        context.setVariable("applyTypeName", applyTypeName);
        context.setVariable("nickName", nickName);
        context.setVariable("projTitle", projTitle);
        context.setVariable("memSortName", memSortName);
        return templateEngine.process(type, context);
    };

    private String bulid(String type, String text){
        Context context = new Context();

        if (type.equalsIgnoreCase("findPw")) {
            context.setVariable("tempPw", text);
        }
        if (type.equalsIgnoreCase("welcomeMail") || type.equalsIgnoreCase("apply")) {
            context.setVariable("nickName", text);
        }

        return templateEngine.process(type, context);
    };
}
