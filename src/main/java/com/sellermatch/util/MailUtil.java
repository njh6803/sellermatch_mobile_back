package com.sellermatch.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class MailUtil{
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public MailUtil(JavaMailSender mailSender, TemplateEngine templateEngine){
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * 메일을 발송한다.
     *
     * @param to - 수신자 메일 주소
     * @param subject  - 제목
     * @param text  - 닉네임 또는 임시비밀번호
     * @param type  - accept, recommand, welcomeMail, findPw
     */
    @Async
    public void sendMail(String to, String subject, String type, String text) {

        MimeMessagePreparator message = mimeMessage -> {
            String content = bulid(type, text);

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setFrom("sellermatch3@daum.net");
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
            messageHelper.setFrom("sellermatch3@daum.net");
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
            messageHelper.setFrom("sellermatch3@daum.net");
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };
        mailSender.send(message);
    }

    private String bulid(String nickName, String type, String applyTypeName){
        Context context = new Context();
        if (!Util.isEmpty(applyTypeName)) {
            context.setVariable("applyTypeName", applyTypeName);
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
        if (type.equalsIgnoreCase("welcomeMail")) {
            context.setVariable("nickName", text);
        }

        return templateEngine.process(type, context);
    };
}
