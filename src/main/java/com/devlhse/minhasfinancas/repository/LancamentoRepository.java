package com.devlhse.minhasfinancas.repository;

import com.devlhse.minhasfinancas.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
