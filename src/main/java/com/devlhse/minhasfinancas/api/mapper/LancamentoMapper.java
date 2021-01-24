package com.devlhse.minhasfinancas.api.mapper;

import com.devlhse.minhasfinancas.api.dto.LancamentoDTO;
import com.devlhse.minhasfinancas.exception.AutorizacaoException;
import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import com.devlhse.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LancamentoMapper {

    private final UsuarioService usuarioService;

    public LancamentoDTO converter(Lancamento lancamento, UUID usuarioId){
        validaUsuario(usuarioId, lancamento);
        return LancamentoDTO.builder()
                .id(lancamento.getId())
                .descricao(lancamento.getDescricao())
                .valor(lancamento.getValor())
                .mes(lancamento.getMes())
                .ano(lancamento.getAno())
                .status(lancamento.getStatus().name())
                .tipo(lancamento.getTipo().name())
                .build();
    }

    public Lancamento converter(LancamentoDTO dto, UUID usuarioId){
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService.obterPorId(usuarioId);

        lancamento.setUsuario(usuario);
        if(dto.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }
        if(dto.getStatus() != null){
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }

    public void validaUsuario(UUID usuarioId, Lancamento entity) {
        if (!entity.getUsuario().getId().equals(usuarioId)) {
            throw new AutorizacaoException("Usuário sem permissão para acessar o recurso.");
        }
    }

}
