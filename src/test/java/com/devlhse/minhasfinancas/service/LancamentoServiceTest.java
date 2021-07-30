package com.devlhse.minhasfinancas.service;

import com.devlhse.minhasfinancas.exception.NotFoundException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import com.devlhse.minhasfinancas.model.repository.LancamentoRepository;
import com.devlhse.minhasfinancas.service.impl.LancamentoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LancamentoServiceTest {

	@InjectMocks
	LancamentoServiceImpl service;

	@Mock
	LancamentoRepository repository;

	@Mock
	private Clock clock;

	private Clock fixedClock;

	@Test
	public void deveSalvarUmLancamento(){
		Lancamento lancamentoASalvar = criarLancamentoValido();
		lancamentoASalvar.setId(UUID.randomUUID());
		service.salvar(lancamentoASalvar);
		verify(repository, times(1)).save(lancamentoASalvar);
	}

	@Test
	public void naoDeveSalvarUmLancamentoQuandoNaoPossuirDescricao(){
		Lancamento lancamentoASalvar = criarLancamentoSemDescricao();
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			service.salvar(lancamentoASalvar);
		});
		verify(repository, times(0)).save(lancamentoASalvar);
	}

	@Test
	public void naoDeveSalvarUmLancamentoComMesInvalido(){
		Lancamento lancamentoASalvar = criarLancamentoComMesInvalido();
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			service.salvar(lancamentoASalvar);
		});
		verify(repository, times(0)).save(lancamentoASalvar);
	}

	@Test
	public void deveAtualizarUmLancamento(){
		Lancamento lancamentoAAtualizar = criarLancamentoValido();
		lancamentoAAtualizar.setId(UUID.randomUUID());
		service.atualizar(lancamentoAAtualizar);
		verify(repository, times(1)).save(lancamentoAAtualizar);
	}

	@Test
	public void naoDeveAtualizarUmLancamentoSemId(){
		Lancamento lancamentoAAtualizar = criarLancamentoValido();
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.atualizar(lancamentoAAtualizar);
		});
		verify(repository, times(0)).save(lancamentoAAtualizar);
	}

	@Test
	public void deveDeletarUmLancamento(){
		Lancamento lancamento = criarLancamentoValido();
		lancamento.setId(UUID.randomUUID());
		service.deletar(lancamento);
		verify(repository, times(1)).delete(lancamento);
	}

	@Test
	public void naoDeveDeletarUmLancamentoSemId(){
		Lancamento lancamento = criarLancamentoValido();
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.deletar(lancamento);
		});
		verify(repository, times(0)).delete(lancamento);
	}

	@Test
	public void deveFiltrarLancamentos(){
		Lancamento lancamento = criarLancamentoValido();
		lancamento.setId(UUID.randomUUID());

		List<Lancamento> lancamentos = Arrays.asList(lancamento);

		when(repository.findAll(any(Example.class))).thenReturn(lancamentos);

		List<Lancamento> resultado = service.buscar(lancamento);

		Assertions.assertEquals(1, resultado.size());
	}

	@Test
	public void deveAtualizarStatusLancamento(){
		Lancamento lancamentoAAtualizar = criarLancamentoValido();
		lancamentoAAtualizar.setId(UUID.randomUUID());
		service.atualizarStatus(lancamentoAAtualizar, StatusLancamento.PENDENTE);
		verify(repository, times(1)).save(lancamentoAAtualizar);
	}

	@Test
	public void naoDeveAtualizarStatusDeUmLancamentoSemId(){
		Lancamento lancamentoAAtualizar = criarLancamentoValido();
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.atualizarStatus(lancamentoAAtualizar, StatusLancamento.PENDENTE);
		});
		verify(repository, times(0)).save(lancamentoAAtualizar);
	}

	@Test
	public void deveObterLancamentoPorId(){
		var lancamentoId = UUID.randomUUID();
		Lancamento lancamento = criarLancamentoValido();
		lancamento.setId(lancamentoId);
		when(repository.findById(any(UUID.class))).thenReturn(Optional.of(lancamento));
		Lancamento lancamentoEncontrado = service.obterPorId(lancamentoId);
		assertNotNull(lancamentoEncontrado);
		assertEquals(lancamentoId, lancamentoEncontrado.getId());
		assertEquals("lançamento teste", lancamentoEncontrado.getDescricao());
		assertEquals(2020, lancamentoEncontrado.getAno());
		assertEquals(1, lancamentoEncontrado.getMes());
	}

	@Test
	public void naoDeveObterLancamentoQuandoIdNaoExistir(){
		var lancamentoId = UUID.randomUUID();
		when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
		Assertions.assertThrows(NotFoundException.class, () -> {
			service.obterPorId(lancamentoId);
		});
	}

	@Test
	public void deveObterSaldo(){
		var usuarioId = UUID.randomUUID();
		when(repository.obterSaldoPorUsuarioETipoEStatus(usuarioId, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO)).thenReturn(BigDecimal.valueOf(400.00));
		when(repository.obterSaldoPorUsuarioETipoEStatus(usuarioId, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO)).thenReturn(BigDecimal.valueOf(100.00));
		BigDecimal saldo = service.obterSaldoPorUsuario(usuarioId);
		assertEquals(BigDecimal.valueOf(300.00), saldo);
	}

	@Test
	public void deveObterSaldoQuandoNaoExistirReceitas(){
		var usuarioId = UUID.randomUUID();
		when(repository.obterSaldoPorUsuarioETipoEStatus(usuarioId, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO)).thenReturn(null);
		when(repository.obterSaldoPorUsuarioETipoEStatus(usuarioId, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO)).thenReturn(BigDecimal.valueOf(100.00));
		BigDecimal saldo = service.obterSaldoPorUsuario(usuarioId);
		assertEquals(BigDecimal.valueOf(-100.00), saldo);
	}

	@Test
	public void deveObterSaldoQuandoNaoExistirDespesas(){
		var usuarioId = UUID.randomUUID();
		when(repository.obterSaldoPorUsuarioETipoEStatus(usuarioId, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO)).thenReturn(BigDecimal.valueOf(400.00));
		when(repository.obterSaldoPorUsuarioETipoEStatus(usuarioId, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO)).thenReturn(null);
		BigDecimal saldo = service.obterSaldoPorUsuario(usuarioId);
		assertEquals(BigDecimal.valueOf(400.00), saldo);
	}

	@Test
	public void deveObterSaldoQuandoNaoExistirReceitasEDespesas(){
		var usuarioId = UUID.randomUUID();
		when(repository.obterSaldoPorUsuarioETipoEStatus(usuarioId, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO)).thenReturn(null);
		when(repository.obterSaldoPorUsuarioETipoEStatus(usuarioId, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO)).thenReturn(null);
		BigDecimal saldo = service.obterSaldoPorUsuario(usuarioId);
		assertEquals(BigDecimal.valueOf(0), saldo);
	}

	@Test
	public void deveDuplicarLancamentosPorUsuarioMesEAnoComSucesso(){

		fixedClock = Clock.fixed(Instant.parse("2020-01-01T10:15:30.00Z"), ZoneId.of("UTC"));
		doReturn(fixedClock.instant()).when(clock).instant();
		doReturn(fixedClock.getZone()).when(clock).getZone();

		var lancamentoId = UUID.randomUUID();
		Lancamento lancamento = criarLancamentoValido();
		lancamento.setId(lancamentoId);
		when(repository.buscarPorUsuarioEMesEAno(lancamento.getUsuario().getId(), lancamento.getMes(), lancamento.getAno()))
				.thenReturn(List.of(lancamento));
		service.duplicarLancamentosMes(lancamento.getUsuario().getId(), lancamento.getMes());
	}


	private Lancamento criarLancamentoValido(){

		Usuario usuarioValido = criarUsuarioValido();

		return Lancamento.builder()
				.ano(2020)
				.mes(1)
				.descricao("lançamento teste")
				.valor(BigDecimal.TEN)
				.tipo(TipoLancamento.DESPESA)
				.usuario(usuarioValido)
				.status(StatusLancamento.CANCELADO)
				.build();
	}

	private Lancamento criarLancamentoSemDescricao(){

		Usuario usuarioValido = criarUsuarioValido();

		return Lancamento.builder()
				.ano(2020)
				.mes(1)
				.valor(BigDecimal.TEN)
				.tipo(TipoLancamento.DESPESA)
				.usuario(usuarioValido)
				.status(StatusLancamento.CANCELADO)
				.build();
	}

	private Lancamento criarLancamentoComMesInvalido(){

		Usuario usuarioValido = criarUsuarioValido();

		return Lancamento.builder()
				.ano(2020)
				.mes(0)
				.descricao("lançamento teste")
				.valor(BigDecimal.TEN)
				.tipo(TipoLancamento.DESPESA)
				.usuario(usuarioValido)
				.status(StatusLancamento.CANCELADO)
				.build();
	}

	private Usuario criarUsuarioValido () {
		return Usuario.builder()
				.id(UUID.randomUUID())
				.nome("teste")
				.senha("teste")
				.email("teste@email.com")
				.build();

	}
}
