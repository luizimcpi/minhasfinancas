package com.devlhse.minhasfinancas.service;

import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);

    Lancamento atualizar(Lancamento lancamento);

    void deletar(Lancamento lancamento);

    List<Lancamento> buscar( Lancamento lancamentoFiltro );

    void atualizarStatus(Lancamento lancamento, StatusLancamento status);

    void validar(Lancamento lancamento);

    Lancamento obterPorId(UUID id);

    BigDecimal obterSaldoPorUsuario(UUID id);

    void duplicarLancamentosMes(UUID usuarioId, Integer mesAtual);
}
