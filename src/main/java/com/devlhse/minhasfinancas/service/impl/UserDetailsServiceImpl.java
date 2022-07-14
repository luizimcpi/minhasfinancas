package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.model.entity.CustomUserDetails;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Usuario> usuario = repository.findByEmail(email.trim());

        if (usuario.isPresent()){
            if(!usuario.get().isValido()) {
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
