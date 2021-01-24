package com.devlhse.minhasfinancas.service;

import com.devlhse.minhasfinancas.model.entity.Usuario;

import java.util.UUID;

public interface UsuarioService {

	Usuario salvar(Usuario usuario);

	void validarEmail(String email);

	Usuario obterPorId(UUID id);
}
