package com.devlhse.minhasfinancas.service;

import com.devlhse.minhasfinancas.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

	Usuario salvar(Usuario usuario);

	void validarEmail(String email);

	Optional<Usuario> obterPorId(Long id);
}
