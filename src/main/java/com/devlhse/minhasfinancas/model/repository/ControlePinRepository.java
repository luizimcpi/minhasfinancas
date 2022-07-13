package com.devlhse.minhasfinancas.model.repository;

import com.devlhse.minhasfinancas.model.entity.ControlePin;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.naming.ldap.Control;
import java.util.Optional;

public interface ControlePinRepository extends JpaRepository<ControlePin, Long> {

    Optional<ControlePin> findByEmail(String email);

    Optional<ControlePin> findByPin(String pin);
}
