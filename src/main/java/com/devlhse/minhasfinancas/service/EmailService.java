package com.devlhse.minhasfinancas.service;

public interface EmailService {

    void enviarEmail(String destinatario, String titulo, String mensagem);
}
