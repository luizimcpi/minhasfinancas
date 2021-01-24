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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class, ComponentTestExtension.class})
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @Rollback
    public void deveSalvarLancamento(){
        Lancamento lancamento = criarLancamento();

        lancamento = repository.save(lancamento);

        assertNotNull(lancamento);
    }

    @Test
    @Rollback
    public void deveDeletarUmLancamento(){
        Lancamento lancamento = criarLancamento();
        entityManager.persist(lancamento);
        lancamento = entityManager.find(Lancamento.class, lancamento.getId());

        repository.delete(lancamento);

        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());

        assertNull(lancamentoInexistente);
    }

    @Test
    @Rollback
    public void deveAtualizarLancamento(){
        Lancamento lancamento = criarLancamento();
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

        Lancamento lancamento = criarLancamento();
        lancamento.setUsuario(usuarioSalvo);
        entityManager.persist(lancamento);

        Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());

        assertTrue(lancamentoEncontrado.isPresent());
    }

    private Lancamento criarLancamento(){

        return Lancamento.builder()
                .ano(2020)
                .mes(1)
                .descricao("lançamento teste")
                .valor(BigDecimal.TEN)
                .tipo(TipoLancamento.DESPESA)
                .status(StatusLancamento.CANCELADO)
                .build();
    }

    private Usuario criarUsuario(){
        return Usuario.builder()
                .id(UUID.randomUUID())
                .nome("teste")
                .email("teste@email.com")
                .senha("12345678")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build();
    }
}
