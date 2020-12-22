package com.devlhse.minhasfinancas.model.repository;

import com.devlhse.minhasfinancas.AbstractIntegrationTest;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	UsuarioRepository repository;

	@Test
	public void deveVerificarAExistenciaDeUmEmail(){
		Usuario ususario = Usuario.builder().nome("usuario").email("usuario@gmail.com").build();
		repository.save(ususario);

		boolean result = repository.existsByEmail("usuario@gmail.com");

		assertTrue(result);
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioComEmail(){

		repository.deleteAll();

		boolean result = repository.existsByEmail("usuario@gmail.com");

		assertFalse(result);
	}
}
