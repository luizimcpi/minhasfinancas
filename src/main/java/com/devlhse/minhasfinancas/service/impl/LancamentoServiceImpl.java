package com.devlhse.minhasfinancas.service.impl;

import com.devlhse.minhasfinancas.exception.NotFoundException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import com.devlhse.minhasfinancas.model.repository.LancamentoRepository;
import com.devlhse.minhasfinancas.service.LancamentoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class LancamentoServiceImpl implements LancamentoService {

    private Clock clock;

    private LancamentoRepository repository;

    public LancamentoServiceImpl(Clock clock, LancamentoRepository repository) {
        this.clock = clock;
        this.repository = repository;
    }

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        validar(lancamento);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        repository.delete(lancamento);
    }

    @Override
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
        lancamento.setStatus(status);
        this.atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {
        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma descrição válida.");
        }

        if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
            throw new RegraNegocioException("Informe um mês válido.");
        }

        if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
            throw new RegraNegocioException("Informe um ano válido.");
        }

        if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um usuário.");
        }

        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um valor válido.");
        }

        if (lancamento.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo de lançamento.");
        }
    }

    @Override
    public Lancamento obterPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lançamento não encotrado!"));
    }

    @Override
    @Transactional
    public BigDecimal obterSaldoPorUsuario(UUID id) {
        BigDecimal receitas = repository.obterSaldoPorUsuarioETipoEStatus(
                id,
                TipoLancamento.RECEITA,
                StatusLancamento.EFETIVADO);
        BigDecimal despesas = repository.obterSaldoPorUsuarioETipoEStatus(
                id,
                TipoLancamento.DESPESA,
                StatusLancamento.EFETIVADO);

        if (receitas == null)
            receitas = BigDecimal.ZERO;
        if (despesas == null)
            despesas = BigDecimal.ZERO;

        return receitas.subtract(despesas);
    }

    @Override
    @Transactional
    public void duplicarLancamentosMes(UUID usuarioId, Integer mesAtual) {
        try {
            var anoAtual = LocalDateTime.now(clock).getYear();
            var novaDataControle = LocalDateTime.now(clock);
            ArrayList<Lancamento> lancamentosParaGravar = new ArrayList<>();

            repository.buscarPorUsuarioEMesEAno(usuarioId, mesAtual, anoAtual).forEach(lancamento -> {
                var lancamentoCopiado = Lancamento.builder()
                        .ano(mesAtual == 12 ? lancamento.getAno() + 1 : anoAtual)
                        .id(UUID.randomUUID())
                        .mes(mesAtual == 12 ? 1 : mesAtual + 1)
                        .dataAlteracao(novaDataControle)
                        .dataCadastro(novaDataControle)
                        .descricao(lancamento.getDescricao())
                        .status(StatusLancamento.PENDENTE)
                        .tipo(lancamento.getTipo())
                        .usuario(lancamento.getUsuario())
                        .valor(lancamento.getValor())
                        .build();
                lancamentosParaGravar.add(lancamentoCopiado);
            });
            if (lancamentosParaGravar.isEmpty()) {
                log.warn("Nenhum lançamento encontrado para duplicar do mes: {} para usuarioId: {}", mesAtual,
                        usuarioId);
                throw new RegraNegocioException("Nenhum lançamento encontrado para duplicar.");
            }
            repository.saveAll(lancamentosParaGravar);

        } catch (Exception e) {
            log.warn("Erro ao duplicar as faturas do mes: {} para usuarioId: {}. Error Message: {}", mesAtual,
                    usuarioId, e.getMessage());
            throw new RegraNegocioException("Ocorreu um erro ao duplicar as suas faturas.");
        }
    }

}
