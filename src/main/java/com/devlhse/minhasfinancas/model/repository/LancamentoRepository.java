package com.devlhse.minhasfinancas.model.repository;

import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    @Query(value =
            "select sum(l.valor) from Lancamento l join l.usuario u" +
                    " WHERE u.id = :idUsuario and l.tipo = :tipo and l.status = :status group by u")
    BigDecimal obterSaldoPorUsuarioETipoEStatus(
            @Param("idUsuario") Long idUsuario,
            @Param("tipo") TipoLancamento tipo,
            @Param("status") StatusLancamento status);
}
