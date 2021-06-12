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
     * @param nickName  - 닉네임
     * @param type  - accept, recommand, welcomeMail
     */
    @Async
    public void sendMail(String to, String subject, String nickName, String type, String applyType) {

        MimeMessagePreparator message = mimeMessage -> {
            String content = bulid(nickName, type, applyType);

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };
//        mailSender.send(message);
    }

    private String bulid(String nickName, String type, String applyTypeName){
        Context context = new Context();
        if (!Util.isEmpty(applyTypeName)) {
            context.setVariable("applyTypeName", applyTypeName);
        }
        context.setVariable("nickName", nickName);
        return templateEngine.process(type, context);
    };
}
