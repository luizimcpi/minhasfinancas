package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.exception.ConflictException;
import com.devlhse.minhasfinancas.exception.NotFoundException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.repository.UsuarioRepository;
import com.devlhse.minhasfinancas.service.CriadorControlePin;
import com.devlhse.minhasfinancas.service.EmailService;
import com.devlhse.minhasfinancas.service.UsuarioService;
import com.devlhse.minhasfinancas.service.VerificadorPin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static com.devlhse.minhasfinancas.utils.RandomUtils.getSixDigitsRandomNumberString;

@Service
@Slf4j
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

	private PasswordEncoder passwordEncoder;
	private UsuarioRepository repository;

	private VerificadorPin verificadorPin;
	private EmailService emailService;
	private CriadorControlePin criadorControlePin;

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

		final var email = usuario.getEmail();
		final var pin = getSixDigitsRandomNumberString();
		final var encodedPin = Base64.getEncoder().encodeToString(pin.getBytes());
		log.warn("Usuario email: {} está inativo publicando evento de envio email de ativacao.", usuario.getEmail());
		emailService.enviarEmail(email,
				"Ativação de usuário - Minhas Finanças",
				"<html><head></head><body>Link de validacão <a href=\"http://localhost:8080/api/usuarios/validar?code="+encodedPin+"\">aqui</a> este link expira em 1 hora.</body></html>");
		criadorControlePin.criaControlePin(email, pin);

		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe){
			throw new ConflictException("Já existe um usuário cadastrado com este email.");
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
