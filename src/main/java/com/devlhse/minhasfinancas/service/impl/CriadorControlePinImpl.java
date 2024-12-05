package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.model.entity.ControlePin;
import com.devlhse.minhasfinancas.model.repository.ControlePinRepository;
import com.devlhse.minhasfinancas.service.CriadorControlePin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CriadorControlePinImpl implements CriadorControlePin {

    private ControlePinRepository repository;

    @Override
    @Async
    @Transactional
    public void criaControlePin(final String email, final String pin) {
        log.debug("Iniciando gravação de novo controle pin para: {}", email);
        try {
            var expiracao = LocalDateTime.now();
            final var controlePin = ControlePin.builder()
                    .pin(pin)
                    .email(email)
                    .dataExpiracao(expiracao.plusMinutes(60))
                    .build();
            Optional<ControlePin> optControlePin = repository.findByEmail(email);
            if (optControlePin.isPresent()) {
                repository.delete(optControlePin.get());
            }
            repository.save(controlePin);
            log.debug("Controle pin gerado com sucesso para: {}", email);
        } catch (Exception e){
            log.warn("Erro ao gravar novo controle pin para email: {}", email);
        }
    }
}
