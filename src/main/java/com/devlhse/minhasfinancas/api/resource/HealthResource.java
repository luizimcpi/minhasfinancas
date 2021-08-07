package com.devlhse.minhasfinancas.api.resource;

import com.devlhse.minhasfinancas.api.dto.HealthDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class HealthResource {

    @GetMapping
    public ResponseEntity health(){
        log.debug("Endpoint de consulta a saúde da aplicação solicitado.");

        return new ResponseEntity(HealthDTO.builder().status("UP").build(), HttpStatus.OK);
    }
}
