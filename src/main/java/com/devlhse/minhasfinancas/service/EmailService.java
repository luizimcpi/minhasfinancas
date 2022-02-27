package com.devlhse.minhasfinancas.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    void enviarEmail(String destinatario, String titulo, String mensagem) throws MessagingException, UnsupportedEncodingException;
}
