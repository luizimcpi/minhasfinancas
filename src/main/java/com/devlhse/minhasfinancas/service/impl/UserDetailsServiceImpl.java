package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.model.entity.CustomUserDetails;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.repository.UsuarioRepository;
import com.devlhse.minhasfinancas.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private UsuarioRepository repository;
    private EmailService emailService;

    public UserDetailsServiceImpl(UsuarioRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isPresent()){
            if(!usuario.get().isAtivo()) {
                emailService.enviarEmail(usuario.get().getEmail(),
                        "Ativação de usuário - Minhas Finanças",
                        "<b>Clique no link de ativação na página de login e utilize o seguinte código: 123456 para ativar seu usuário na página de login.</b>");
                log.warn("Usuario id: {} está inativo publicando evento de envio email.", usuario.get().getId());
                throw new InsufficientAuthenticationException("Usuario Inativo, favor ativar no link enviado para o email");
            }
            var userDetails = new CustomUserDetails(usuario.get(),
                    usuario.get().isAtivo(), true, true, true,
                    new ArrayList<>());
             return userDetails;
        }else{
            throw new UsernameNotFoundException(String.format("Username not found"));
        }
    }
}
