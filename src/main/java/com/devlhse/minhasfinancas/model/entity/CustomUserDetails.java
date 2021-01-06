package com.devlhse.minhasfinancas.model.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private Usuario usuario;

    public CustomUserDetails(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        super(usuario.getEmail(), usuario.getSenha(), authorities);
        this.usuario = usuario;
    }

    public CustomUserDetails(Usuario usuario, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(usuario.getEmail(), usuario.getSenha(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
