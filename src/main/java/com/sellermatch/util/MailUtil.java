package com.sellermatch.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class MailUtil{
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 메일을 발송한다.
     *
     * @param receiver - 수신자 메일 주소
     * @param subject  - 제목
     * @param content  - 내용
     */
    public void sendMail(String receiver, String subject, String content) throws Exception{

        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
        String sender = mailSenderImpl.getUsername();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject(subject);
        helper.setText(content, true);	// true는 content에서 HTML태그를 허용하겠다는 의미
        helper.setFrom(new InternetAddress(sender));
        helper.setTo(new InternetAddress(receiver));

        mailSender.send(message);

    }
}
