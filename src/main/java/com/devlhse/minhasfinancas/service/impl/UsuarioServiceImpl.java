package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.exception.AutenticacaoException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.repository.UsuarioRepository;
import com.devlhse.minhasfinancas.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private PasswordEncoder passwordEncoder;
	private UsuarioRepository repository;

	public UsuarioServiceImpl(PasswordEncoder passwordEncoder, UsuarioRepository repository) {
		this.passwordEncoder = passwordEncoder;
		this.repository = repository;
	}


	@Override
	public Usuario auntenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);

		if(!usuario.isPresent()){
			throw new AutenticacaoException("Usuario não encontrado para o email informado.");
		}

		if(!passwordEncoder.matches(senha, usuario.get().getSenha())){
			throw new AutenticacaoException("Senha inválida");
		}

		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvar(Usuario usuarioResource) {
		Usuario usuario = Usuario.builder()
				.nome(usuarioResource.getNome())
				.email(usuarioResource.getEmail())
				.senha(passwordEncoder.encode(usuarioResource.getSenha()))
				.senha(usuarioResource.getSenha())
				.build();
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

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}
}
