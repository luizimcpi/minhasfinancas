package com.devlhse.minhasfinancas.model.repository;

import com.devlhse.minhasfinancas.ComponentTestExtension;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith({SpringExtension.class, ComponentTestExtension.class})
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;

	@Autowired
	TestEntityManager entityManager;


	@Test
	@Rollback
	public void deveVerificarAExistenciaDeUmEmail(){
		Usuario usuario = criarUsuario();

		entityManager.persist(usuario);

		boolean result = repository.existsByEmail("usuario@email.com");

		assertTrue(result);
	}

	@Test
	@Rollback
	public void deveRetornarFalsoQuandoNaoHouverUsuarioComEmail(){

		boolean result = repository.existsByEmail("usuario@gmail.com");

		assertFalse(result);
	}

	@Test
	@Rollback
	public void devePersistirUmUsuarioNaBaseDeDados(){
		Usuario usuario = criarUsuario();

		Usuario usuarioSalvo = repository.save(usuario);

		Assertions.assertNotNull(usuarioSalvo);
	}

	@Test
	@Rollback
	public void deveBuscarUmUsuarioPorEmail(){
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);

		Optional<Usuario> usuarioRetornado = repository.findByEmail("usuario@email.com");

		assertTrue(usuarioRetornado.isPresent());

	}

	@Test
	@Rollback
	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExisteNaBase(){

		Optional<Usuario> usuarioRetornado = repository.findByEmail("usuario@email.com");

		assertFalse(usuarioRetornado.isPresent());

	}

	public static Usuario criarUsuario(){
		return Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
}
