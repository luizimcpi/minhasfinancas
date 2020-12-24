package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.exception.AutenticacaoException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.repository.UsuarioRepository;
import com.devlhse.minhasfinancas.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public Usuario auntenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);

		if(!usuario.isPresent()){
			throw new AutenticacaoException("Usuario não encontrado para o email informado.");
		}

		if(!usuario.get().getSenha().equals(senha)){
			throw new AutenticacaoException("Senha inválida");
		}

		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvar(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe){
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
	}
}
