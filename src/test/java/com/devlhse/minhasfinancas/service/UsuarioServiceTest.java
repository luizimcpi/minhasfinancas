package com.devlhse.minhasfinancas.service;

import com.devlhse.minhasfinancas.exception.ConflictException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.repository.UsuarioRepository;
import com.devlhse.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

	@InjectMocks
	UsuarioServiceImpl service;

	@Mock
	UsuarioRepository repository;

	@Mock
	PasswordEncoder passwordEncoder;

	@Mock
	EmailService emailService;

	@Mock
	CriadorControlePin criadorControlePin;

	@Test
	public void deveValidarEmail(){
		var validEmail = "email@email.com";
		when(repository.existsByEmail(validEmail)).thenReturn(false);
		service.validarEmail(validEmail);
		verify(repository, times(1)).existsByEmail(validEmail);
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
		var validEmail = "email@email.com";
		when(repository.existsByEmail(validEmail)).thenReturn(true);
		Assertions.assertThrows(ConflictException.class, () -> {
			service.validarEmail(validEmail);
		});
	}

	@Test
	public void deveSalvarUmUsuario(){
		String validEmail = "email@email.com";
		when(repository.existsByEmail(validEmail)).thenReturn(false);
		Usuario usuario = Usuario.builder().nome("nome").email(validEmail).senha("senha").build();
		when(repository.save(any())).thenReturn(usuario);

		Usuario usuarioSalvo = service.salvar(usuario);

		assertNotNull(usuarioSalvo);
		assertEquals("nome", usuarioSalvo.getNome());
		assertEquals("email@email.com", usuarioSalvo.getEmail());
		assertEquals("senha", usuarioSalvo.getSenha());
//		verify(emailService, times(1)).enviarEmail(anyString(), anyString(), anyString());
//		verify(criadorControlePin, times(1)).criaControlePin(anyString(), anyString());
	}

	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado(){
		String validEmail = "email@email.com";
		when(repository.existsByEmail(validEmail)).thenReturn(true);
		Usuario usuario = Usuario.builder().nome("nome").email(validEmail).senha("senha").build();

		Assertions.assertThrows(ConflictException.class, () -> {
			service.salvar(usuario);
		});

		verify(repository, never()).save(usuario);
		verify(emailService, never()).enviarEmail(anyString(), anyString(), anyString());
		verify(criadorControlePin, never()).criaControlePin(anyString(), anyString());
	}
}
