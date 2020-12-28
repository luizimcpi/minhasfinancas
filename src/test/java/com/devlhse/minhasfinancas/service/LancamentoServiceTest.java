package com.devlhse.minhasfinancas.service;

import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import com.devlhse.minhasfinancas.model.repository.LancamentoRepository;
import com.devlhse.minhasfinancas.service.impl.LancamentoServiceImpl;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LancamentoServiceTest {

	@InjectMocks
	LancamentoServiceImpl service;

	@Mock
	LancamentoRepository repository;

	@Test
	public void deveSalvarUmLancamento(){
		Lancamento lancamentoASalvar = criarLancamentoValido();
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
		service.atualizar(lancamentoAAtualizar);
		verify(repository, times(1)).save(lancamentoAAtualizar);
	}

	@Test
	public void naoDeveAtualizarUmLancamentoSemId(){
		Lancamento lancamentoAAtualizar = criarLancamentoSemId();
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.atualizar(lancamentoAAtualizar);
		});
		verify(repository, times(0)).save(lancamentoAAtualizar);
	}

	private Lancamento criarLancamentoValido(){

		Usuario usuarioValido = criarUsuarioValido();

		return Lancamento.builder()
				.id(1l)
				.ano(2020)
				.mes(1)
				.descricao("lançamento teste")
				.valor(BigDecimal.TEN)
				.tipo(TipoLancamento.DESPESA)
				.usuario(usuarioValido)
				.status(StatusLancamento.CANCELADO)
				.build();
	}

	private Lancamento criarLancamentoSemId(){

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
