package com.devlhse.minhasfinancas.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UsuarioLoginDTO {
    private String email;
    private String senha;
}
