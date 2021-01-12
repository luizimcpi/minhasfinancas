package com.devlhse.minhasfinancas.model.repository;

import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.devlhse.minhasfinancas.config.CacheConfiguration.SALDO_USUARIO;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    @Cacheable(cacheNames = { SALDO_USUARIO })
    @Query(value =
            "select sum(l.valor) from Lancamento l join l.usuario u" +
                    " WHERE u.id = :idUsuario and l.tipo = :tipo and l.status = :status group by u")
    BigDecimal obterSaldoPorUsuarioETipoEStatus(
            @Param("idUsuario") UUID idUsuario,
            @Param("tipo") TipoLancamento tipo,
            @Param("status") StatusLancamento status);

    Optional<Lancamento> findById(UUID id);

}
