package com.devlhse.minhasfinancas.model.repository;

import com.devlhse.minhasfinancas.AbstractIntegrationTest;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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
		Usuario usuario = criarUsuario();

		entityManager.persist(usuario);

		boolean result = repository.existsByEmail("usuario@email.com");

		assertTrue(result);
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioComEmail(){

		boolean result = repository.existsByEmail("usuario@gmail.com");

		assertFalse(result);
	}

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados(){
		Usuario usuario = criarUsuario();

		Usuario usuarioSalvo = repository.save(usuario);

		Assertions.assertNotNull(usuarioSalvo);
	}

	@Test
	public void deveBuscarUmUsuarioPorEmail(){
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);

		Optional<Usuario> usuarioRetornado = repository.findByEmail("usuario@email.com");

		assertTrue(usuarioRetornado.isPresent());

	}

	@Test
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
