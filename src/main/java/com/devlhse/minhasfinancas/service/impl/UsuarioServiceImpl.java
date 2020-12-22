package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.repository.UsuarioRepository;
import com.devlhse.minhasfinancas.service.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public Usuario auntenticar(String email, String senha) {
		return null;
	}

	@Override
	public Usuario salvar(Usuario usuario) {
		return null;
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe){
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
	}
}
