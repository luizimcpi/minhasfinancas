package com.devlhse.minhasfinancas.service;

import com.devlhse.minhasfinancas.service.impl.GmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    GmailServiceImpl emailService;

    @Mock
    private JavaMailSender mailSender;

    private MimeMessage mimeMessage;

    @Test
    public void deveEnviarUmEmailComSucesso() {
        emailService = new GmailServiceImpl(mailSender, "teste");
        mimeMessage = new MimeMessage((Session)null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService.enviarEmail("email@email.com", "Minhas Finanças", "<b>Teste</b>");
        verify( mailSender, times(1)).send(any(MimeMessage.class));
    }

}
