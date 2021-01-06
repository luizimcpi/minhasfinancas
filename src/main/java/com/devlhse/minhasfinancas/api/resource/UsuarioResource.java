package com.devlhse.minhasfinancas.api.resource;

import com.devlhse.minhasfinancas.api.dto.UsuarioDTO;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.service.LancamentoService;
import com.devlhse.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

    private final UsuarioService service;
    private final LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto){

       Usuario usuario = Usuario.builder()
               .nome(dto.getNome())
               .email(dto.getEmail())
               .senha(dto.getSenha())
               .build();

       try {
           Usuario usuarioSalvo = service.salvar(usuario);
           return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
       } catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

//    @PostMapping("/autenticar")
//    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto){
//
//        try {
//            Usuario usuarioAutenticado = service.auntenticar(dto.getEmail(), dto.getSenha());
//            return ResponseEntity.ok(usuarioAutenticado);
//        }catch (AutenticacaoException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    @GetMapping("{id}/saldo")
    public ResponseEntity obterSaldo(@PathVariable("id") Long id){
        Optional<Usuario> usuario = service.obterPorId(id);

        if(usuario.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }
}
