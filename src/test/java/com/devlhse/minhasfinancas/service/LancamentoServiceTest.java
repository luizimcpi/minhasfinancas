package com.devlhse.minhasfinancas.service;

import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import com.devlhse.minhasfinancas.model.repository.LancamentoRepository;
import com.devlhse.minhasfinancas.service.impl.LancamentoServiceImpl;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
public class LancamentoServiceTest {

	@InjectMocks
	LancamentoServiceImpl service;

	@Mock
	LancamentoRepository repository;

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
		Optional<Lancamento> lancamentoEncontrado = service.obterPorId(lancamentoId);
		assertTrue(lancamentoEncontrado.isPresent());
		assertEquals(lancamentoId, lancamentoEncontrado.get().getId());
		assertEquals("lançamento teste", lancamentoEncontrado.get().getDescricao());
		assertEquals(2020, lancamentoEncontrado.get().getAno());
		assertEquals(1, lancamentoEncontrado.get().getMes());
	}

	@Test
	public void naoDeveObterLancamentoQuandoIdNaoExistir(){
		var lancamentoId = UUID.randomUUID();
		when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
		Optional<Lancamento> lancamentoEncontrado = service.obterPorId(lancamentoId);
		assertTrue(lancamentoEncontrado.isEmpty());
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
