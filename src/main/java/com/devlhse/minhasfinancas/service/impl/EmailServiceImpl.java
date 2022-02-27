package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${from.email.address}")
    private String fromEmailAddress;

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void enviarEmail(String destinatario, String titulo, String mensagem) throws MessagingException, UnsupportedEncodingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromEmailAddress, "Minhas Finanças");
            helper.setTo(destinatario);
            helper.setSubject(titulo);
            helper.setText(mensagem, true);
            mailSender.send(message);
            log.info("Sucesso ao enviar email para o destinatario: {}", destinatario);
        } catch (Exception e){
            log.warn("Erro ao enviar mensagem para destinario: {} erro: {}", destinatario, e.getMessage());
            throw e;
        }

    }

}
