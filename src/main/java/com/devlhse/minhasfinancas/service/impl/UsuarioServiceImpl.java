package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.exception.NotFoundException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.repository.UsuarioRepository;
import com.devlhse.minhasfinancas.service.UsuarioService;
import com.devlhse.minhasfinancas.service.VerificadorPin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private PasswordEncoder passwordEncoder;
	private UsuarioRepository repository;

	private VerificadorPin verificadorPin;

	public UsuarioServiceImpl(PasswordEncoder passwordEncoder,
							  UsuarioRepository repository,
							  VerificadorPin verificadorPin) {
		this.passwordEncoder = passwordEncoder;
		this.repository = repository;
		this.verificadorPin = verificadorPin;
	}

	@Override
	@Transactional
	public Usuario salvar(Usuario usuarioResource) {
		Usuario usuario = Usuario.builder()
				.nome(usuarioResource.getNome())
				.email(usuarioResource.getEmail())
				.senha(passwordEncoder.encode(usuarioResource.getSenha()))
				.ativo(false)
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
	public Usuario obterPorId(UUID id) {
		return repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Usuário não encontrado para id informado!"));
	}

	@Override
	public void validar(String encodedCode) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedCode);
		String decodedCode = new String(decodedBytes);
		String email = verificadorPin.validar(decodedCode);
		Optional<Usuario> optionalUsuario = repository.findByEmail(email);
		if(optionalUsuario.isPresent()){
			final var usuarioBD = optionalUsuario.get();

			Usuario usuarioValidado = Usuario.builder()
					.ativo(true)
					.dataCadastro(usuarioBD.getDataCadastro())
					.nome(usuarioBD.getNome())
					.email(usuarioBD.getEmail())
					.id(usuarioBD.getId())
					.senha(usuarioBD.getSenha())
					.build();

			repository.save(usuarioValidado);
		}
	}
}
