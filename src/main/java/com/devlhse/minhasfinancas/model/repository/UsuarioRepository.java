package com.devlhse.minhasfinancas.model.repository;

import com.devlhse.minhasfinancas.model.entity.Usuario;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import static com.devlhse.minhasfinancas.config.CacheConfiguration.LOGIN_USUARIO;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	boolean existsByEmail(String email);

	@Cacheable(cacheNames = { LOGIN_USUARIO })
	Optional<Usuario> findByEmail(String email);

	Optional<Usuario> findById(UUID id);
}
