package com.devlhse.minhasfinancas.model.repository;

import com.devlhse.minhasfinancas.AbstractIntegrationTest;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	UsuarioRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveVerificarAExistenciaDeUmEmail(){
		Usuario ususario = Usuario.builder().nome("usuario").email("usuario@gmail.com").build();

		entityManager.persist(ususario);

		boolean result = repository.existsByEmail("usuario@gmail.com");

		assertTrue(result);
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioComEmail(){

		boolean result = repository.existsByEmail("usuario@gmail.com");

		assertFalse(result);
	}
}
