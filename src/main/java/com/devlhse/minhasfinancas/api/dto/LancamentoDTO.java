package com.devlhse.minhasfinancas.api.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LancamentoDTO {

    private UUID id;
    private String descricao;
    private Integer mes;
    private Integer ano;
    private BigDecimal valor;
    private String tipo;
    private String status;
}
