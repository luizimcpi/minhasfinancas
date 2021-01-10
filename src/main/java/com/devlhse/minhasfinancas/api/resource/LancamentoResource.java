package com.devlhse.minhasfinancas.api.resource;

import com.devlhse.minhasfinancas.api.dto.LancamentoDTO;
import com.devlhse.minhasfinancas.api.dto.LancamentoStatusDTO;
import com.devlhse.minhasfinancas.exception.AutorizacaoException;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Lancamento;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import com.devlhse.minhasfinancas.service.LancamentoService;
import com.devlhse.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
@Slf4j
public class LancamentoResource {

    private final LancamentoService service;
    private final UsuarioService usuarioService;

    @GetMapping("{id}")
    public ResponseEntity obterLancamento(@PathVariable("id") Long id,
                                          @RequestHeader("usuarioId") Long usuarioId){
        log.debug("Iniciando busca de lançamento com id: {} e usuarioId: {}", id, usuarioId);
        try {
            return service.obterPorId(id)
                    .map(lancamento -> new ResponseEntity(converter(lancamento, usuarioId), HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
        } catch (AutorizacaoException e){
            log.debug("Erro na autorização de busca de lançamento com id: {} e usuarioId: {}", id, usuarioId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity salvar(@RequestHeader("usuarioId") Long usuarioId,
                                 @RequestBody LancamentoDTO dto){
        log.debug("Iniciando cadastro de lançamento para usuarioId: {}", usuarioId);
        try {
            Lancamento lancamento = converter(dto, usuarioId);
            lancamento = service.salvar(lancamento);
            return new ResponseEntity(lancamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            log.debug("Erro no cadastro de lançamento para usuarioId: {}", usuarioId);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@RequestHeader("usuarioId") Long usuarioId,
                                    @PathVariable("id") Long id,
                                    @RequestBody LancamentoDTO dto) {
        log.debug("Iniciando alteração de lançamento id: {} para usuarioId: {}", id, usuarioId);
        try {
            return service.obterPorId(id).map(entity -> {
                    validaUsuario(usuarioId, entity);
                    Lancamento lancamento = converter(dto, usuarioId);
                    lancamento.setId(entity.getId());
                    lancamento.setDataCadastro(entity.getDataCadastro());
                    lancamento = service.atualizar(lancamento);
                    return ResponseEntity.ok(lancamento);
            }).orElseGet(() ->
                    new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.NOT_FOUND));
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AutorizacaoException e) {
            log.debug("Erro na autorização de busca de lançamento com id: {} e usuarioId: {}", id, usuarioId);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@RequestHeader("usuarioId") Long usuarioId,
                                  @PathVariable("id") Long id){
        log.debug("Iniciando deleção de lançamento id: {} para usuarioId: {}", id, usuarioId);
        try {
            return service.obterPorId(id).map(entity -> {
                validaUsuario(usuarioId, entity);
                service.deletar(entity);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }).orElseGet(() ->
                    new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.NOT_FOUND));
        }catch (AutorizacaoException e){
            log.debug("Erro na autorização de busca de lançamento com id: {} e usuarioId: {}", id, usuarioId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
                                 @RequestParam(value = "mes", required = false) Integer mes,
                                 @RequestParam(value = "ano", required = false) Integer ano,
                                 @RequestHeader("usuarioId") Long usuarioId){

        log.debug("Iniciando busca de lançamentos para usuarioId: {}", usuarioId);

        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(usuarioId);
        if(usuario.isEmpty()){
            return ResponseEntity.badRequest().body("Usuário não encontrado para o Id informado.");
        }else{
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);

    }

    @PutMapping("{id}/status")
    public ResponseEntity atualizarStatus( @RequestHeader("usuarioId") Long usuarioId,
                                           @PathVariable("id") Long id,
                                           @RequestBody LancamentoStatusDTO dto){

        log.debug("Iniciando alteração status de lançamento {} para usuarioId: {}", id, usuarioId);

        return service.obterPorId(id).map(entity -> {
            StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
            if(statusSelecionado == null){
                return ResponseEntity.badRequest().body("Não foi possível atualizar status do lançamento.");
            }
            try {
                validaUsuario(usuarioId, entity);
                entity.setStatus(statusSelecionado);
                service.atualizar(entity);
                return ResponseEntity.ok(entity);
            }catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }catch(AutorizacaoException e){
                log.debug("Erro na autorização de busca de lançamento com id: {} e usuarioId: {}", id, usuarioId);
                return ResponseEntity.notFound().build();
            }
        }).orElseGet(() ->
                new ResponseEntity("Lançamento não encontrado na base de Dados.", HttpStatus.NOT_FOUND));
    }

    private LancamentoDTO converter(Lancamento lancamento, Long usuarioId){
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

    private Lancamento converter(LancamentoDTO dto, Long usuarioId){
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService.obterPorId(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para id informado."));

        lancamento.setUsuario(usuario);
        if(dto.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }
        if(dto.getStatus() != null){
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }

    private void validaUsuario(@RequestHeader("usuarioId") Long usuarioId, Lancamento entity) {
        if (!entity.getUsuario().getId().equals(usuarioId)) {
            throw new AutorizacaoException("Usuário sem permissão para acessar o recurso.");
        }
    }
}
