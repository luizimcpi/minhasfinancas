package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.config.EmailConfig;
import com.devlhse.minhasfinancas.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Slf4j
@Async
@AllArgsConstructor
public class GmailEmailServiceImpl implements EmailService {

    private EmailConfig emailConfig;

    @Override
    public void enviarEmail(String destinatario, String titulo, String mensagem) {
        log.debug("Iniciando envio do email para: {}", destinatario);
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getUserEmail(), emailConfig.getUserPassword());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailConfig.getUserEmail()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinatario));
            message.setSubject(titulo);
            message.setText(mensagem);

            Transport.send(message);

            log.debug("Email enviado com sucesso para: {}", destinatario);

        } catch (MessagingException e) {
            log.warn("Erro ao enviar email para: {} erro {}", destinatario, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
