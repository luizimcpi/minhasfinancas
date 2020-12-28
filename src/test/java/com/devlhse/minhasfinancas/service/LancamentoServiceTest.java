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
		lancamentoASalvar.setId(1l);
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
		lancamentoAAtualizar.setId(1l);
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
		lancamento.setId(1l);
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
		lancamento.setId(1l);

		List<Lancamento> lancamentos = Arrays.asList(lancamento);

		when(repository.findAll(any(Example.class))).thenReturn(lancamentos);

		List<Lancamento> resultado = service.buscar(lancamento);

		Assertions.assertEquals(1, resultado.size());
	}

	@Test
	public void deveAtualizarStatusLancamento(){
		Lancamento lancamentoAAtualizar = criarLancamentoValido();
		lancamentoAAtualizar.setId(1l);
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
		Lancamento lancamento = criarLancamentoValido();
		lancamento.setId(1l);
		when(repository.findById(anyLong())).thenReturn(Optional.of(lancamento));
		Optional<Lancamento> lancamentoEncontrado = service.obterPorId(1l);
		assertTrue(lancamentoEncontrado.isPresent());
		assertEquals(1l, lancamentoEncontrado.get().getId());
		assertEquals("lançamento teste", lancamentoEncontrado.get().getDescricao());
		assertEquals(2020, lancamentoEncontrado.get().getAno());
		assertEquals(1, lancamentoEncontrado.get().getMes());
	}

	@Test
	public void naoDeveObterLancamentoQuandoIdNaoExistir(){
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		Optional<Lancamento> lancamentoEncontrado = service.obterPorId(1l);
		assertTrue(lancamentoEncontrado.isEmpty());
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
				.id(1l)
				.nome("teste")
				.senha("teste")
				.email("teste@email.com")
				.build();

	}
}
