package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.model.entity.CustomUserDetails;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UsuarioRepository repository;

    public UserDetailsServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isPresent()){
            var userDetails = new CustomUserDetails(usuario.get(),
                    true, true, true, true,
                    new ArrayList<>());
             return userDetails;
        }else{
            throw new UsernameNotFoundException(String.format("Username[%s] not found"));
        }
    }
}
