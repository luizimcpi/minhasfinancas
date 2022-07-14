package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.exception.NotFoundException;
import com.devlhse.minhasfinancas.exception.PinNotFoundException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.exception.ValidacaoUsuarioException;
import com.devlhse.minhasfinancas.model.entity.ControlePin;
import com.devlhse.minhasfinancas.model.repository.ControlePinRepository;
import com.devlhse.minhasfinancas.service.CriadorControlePin;
import com.devlhse.minhasfinancas.service.VerificadorPin;
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
public class VerificadorPinImpl implements VerificadorPin {

    private ControlePinRepository repository;

    @Override
    public String validar(String pin) {
        Optional<ControlePin> optionalControlePin = repository.findByPin(pin);
        if(optionalControlePin.isPresent()){
            var now = LocalDateTime.now();
            var controlePin = optionalControlePin.get();
            if(now.isAfter(controlePin.getDataExpiracao())){
                throw new ValidacaoUsuarioException("Link para ativacao expirado");
            }
            repository.delete(controlePin);
            return controlePin.getEmail();
        } else {
            throw new PinNotFoundException("Não foi encontrado registro para o codigo informado.");
        }
    }
}
