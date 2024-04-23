package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SendgridServiceImpl implements EmailService {

    @Async
    @Override
    public void enviarEmail(String destinatario, String titulo, String mensagem) {

        Email from = new Email("luizimcpi@gmail.com");
        Email to = new Email(destinatario);
        Content content = new Content("text/html", mensagem);
        Mail mail = new Mail(from, titulo, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("Status Code {}, Body {}, Headers {}", response.getStatusCode(), response.getBody(), response.getHeaders());
            log.info("Sucesso ao enviar email para o destinatario: {}", destinatario);
        } catch (IOException ex) {
            log.warn("Erro ao enviar mensagem para destinario: {} erro: {}", destinatario, ex.getMessage());
        }

    }

}
