package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.config.EmailConfig;
import com.devlhse.minhasfinancas.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.Address;
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
public class GmailEmailServiceImpl implements EmailService {

    private EmailConfig emailConfig;

    public GmailEmailServiceImpl(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    @Override
    public void enviarEmail(String destinatario, String titulo, String corpo) {
        log.debug("Iniciando envio do email para: {}", destinatario);
        Properties props = new Properties();
        /** Parâmetros de conexão com servidor Gmail */
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(emailConfig.getUserEmail(),
                                emailConfig.getUserPassword());
                    }
                });

        /** Ativa Debug para sessão */
        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("seuemail@gmail.com"));
            //Remetente

            Address[] toUser = InternetAddress
                    .parse("seuamigo@gmail.com, seucolega@hotmail.com, seuparente@yahoo.com.br");

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(titulo);//Assunto
            message.setText(corpo);
            /**Método para enviar a mensagem criada*/
            Transport.send(message);

            log.debug("Email enviado com sucesso para: {}", destinatario);

        } catch (MessagingException e) {
            log.warn("Erro ao enviar email para: {} ", destinatario);
            throw new RuntimeException(e);
        }
    }
}
