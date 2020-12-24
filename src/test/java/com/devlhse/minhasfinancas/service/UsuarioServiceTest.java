package com.devlhse.minhasfinancas.service;

import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.repository.UsuarioRepository;
import com.devlhse.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

	@InjectMocks UsuarioServiceImpl service;

	@Mock
	UsuarioRepository repository;

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
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			service.validarEmail(validEmail);
		});
	}
}
