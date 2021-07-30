package com.devlhse.minhasfinancas.model.repository;

import com.devlhse.minhasfinancas.ComponentTestExtension;
import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class, ComponentTestExtension.class})
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LancamentoRepositoryTest {

    public static final UUID USUARIO_ID = UUID.fromString("999ebe8b-2a10-4405-92d8-d4decd9277da");
    @Autowired
    LancamentoRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @Rollback
    public void deveSalvarLancamento(){
        Lancamento lancamento = criarLancamento(2020);

        lancamento = repository.save(lancamento);

        assertNotNull(lancamento);
    }

    @Test
    @Rollback
    public void deveDeletarUmLancamento(){
        Lancamento lancamento = criarLancamento(2020);
        entityManager.persist(lancamento);
        lancamento = entityManager.find(Lancamento.class, lancamento.getId());

        repository.delete(lancamento);

        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());

        assertNull(lancamentoInexistente);
    }

    @Test
    @Rollback
    public void deveAtualizarLancamento(){
        Lancamento lancamento = criarLancamento(2020);
        entityManager.persist(lancamento);

        lancamento.setStatus(StatusLancamento.EFETIVADO);
        lancamento.setAno(2019);
        lancamento.setDescricao("lançamento alterado");
        repository.save(lancamento);

        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());

        assertEquals(StatusLancamento.EFETIVADO, lancamentoAtualizado.getStatus());
        assertEquals(2019, lancamentoAtualizado.getAno());
        assertEquals("lançamento alterado", lancamentoAtualizado.getDescricao());
    }

    @Test
    @Rollback
    public void deveBuscarUmLancamentoPorId(){
        Usuario usuario = criarUsuario();

        var usuarioSalvo = usuarioRepository.save(usuario);

        Lancamento lancamento = criarLancamento(2020);
        lancamento.setUsuario(usuarioSalvo);
        entityManager.persist(lancamento);

        Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());

        assertTrue(lancamentoEncontrado.isPresent());
    }

    @Test
    @Rollback
    public void deveBuscarLancamentosPorUsuarioEMes(){
        Usuario usuario = criarUsuario();

        var usuarioSalvo = usuarioRepository.save(usuario);

        Lancamento lancamento2020 = criarLancamento(2020);
        lancamento2020.setUsuario(usuarioSalvo);
        entityManager.persist(lancamento2020);

        Lancamento lancamento2021 = criarLancamento(2021);
        lancamento2021.setUsuario(usuarioSalvo);
        entityManager.persist(lancamento2021);

        List<Lancamento> lancamentos = repository.buscarPorUsuarioEMesEAno(lancamento2020.getUsuario().getId(), 1, 2020);

        assertTrue(lancamentos.size() == 1);
        assertEquals(1, lancamentos.get(0).getMes());
        assertEquals(2020, lancamentos.get(0).getAno());
    }

    private Lancamento criarLancamento(Integer ano){

        return Lancamento.builder()
                .ano(ano)
                .mes(1)
                .descricao("lançamento teste")
                .valor(BigDecimal.TEN)
                .tipo(TipoLancamento.DESPESA)
                .status(StatusLancamento.CANCELADO)
                .build();
    }

    private Usuario criarUsuario(){
        return Usuario.builder()
                .id(USUARIO_ID)
                .nome("teste")
                .email("teste@email.com")
                .senha("12345678")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build();
    }
}
