package com.devlhse.minhasfinancas.api.resource;

import com.devlhse.minhasfinancas.api.dto.LancamentoDTO;
import com.devlhse.minhasfinancas.api.dto.LancamentoStatusDTO;
import com.devlhse.minhasfinancas.api.mapper.LancamentoMapper;
import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import com.devlhse.minhasfinancas.service.LancamentoService;
import com.devlhse.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
@Slf4j
public class LancamentoResource {

    private final LancamentoService service;
    private final UsuarioService usuarioService;
    private final LancamentoMapper lancamentoMapper;

    @GetMapping("{id}")
    public ResponseEntity obterLancamento(@PathVariable("id") UUID id,
                                          @RequestHeader("usuarioId") UUID usuarioId){
        log.debug("Iniciando busca de lançamento com id: {} e usuarioId: {}", id, usuarioId);

        return new ResponseEntity(lancamentoMapper.converter(service.obterPorId(id), usuarioId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestHeader("usuarioId") UUID usuarioId,
                                 @RequestBody LancamentoDTO dto){

        log.debug("Iniciando cadastro de lançamento para usuarioId: {}", usuarioId);

        Lancamento lancamento = lancamentoMapper.converter(dto, usuarioId);
        lancamento = service.salvar(lancamento);
        return new ResponseEntity(lancamento, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@RequestHeader("usuarioId") UUID usuarioId,
                                    @PathVariable("id") UUID id,
                                    @RequestBody LancamentoDTO dto) {
        log.debug("Iniciando alteração de lançamento id: {} para usuarioId: {}", id, usuarioId);

        var entity = service.obterPorId(id);
        lancamentoMapper.validaUsuario(usuarioId, entity);
        Lancamento lancamento = lancamentoMapper.converter(dto, usuarioId);
        lancamento.setId(entity.getId());
        lancamento.setDataCadastro(entity.getDataCadastro());
        lancamento = service.atualizar(lancamento);
        return ResponseEntity.ok(lancamento);

    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@RequestHeader("usuarioId") UUID usuarioId,
                                  @PathVariable("id") UUID id){
        log.debug("Iniciando deleção de lançamento id: {} para usuarioId: {}", id, usuarioId);

        var entity = service.obterPorId(id);
        lancamentoMapper.validaUsuario(usuarioId, entity);
        service.deletar(entity);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
                                 @RequestParam(value = "mes", required = false) Integer mes,
                                 @RequestParam(value = "ano", required = false) Integer ano,
                                 @RequestParam(value = "tipo", required = false) String tipo,
                                 @RequestHeader("usuarioId") UUID usuarioId){

        log.debug("Iniciando busca de lançamentos para usuarioId: {}", usuarioId);

        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);
        if(StringUtils.hasLength(tipo)) {
            lancamentoFiltro.setTipo(TipoLancamento.valueOf(tipo));
        }

        var usuario = usuarioService.obterPorId(usuarioId);
        lancamentoFiltro.setUsuario(usuario);

        List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);

    }

    @PutMapping("{id}/status")
    public ResponseEntity atualizarStatus( @RequestHeader("usuarioId") UUID usuarioId,
                                           @PathVariable("id") UUID id,
                                           @RequestBody LancamentoStatusDTO dto){

        log.debug("Iniciando alteração status de lançamento {} para usuarioId: {}", id, usuarioId);

        var entity = service.obterPorId(id);
        StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
        if(statusSelecionado == null){
            return ResponseEntity.badRequest().body("Não foi possível atualizar status do lançamento.");
        }

        lancamentoMapper.validaUsuario(usuarioId, entity);
        entity.setStatus(statusSelecionado);
        service.atualizar(entity);
        return ResponseEntity.ok(entity);
    }

}
