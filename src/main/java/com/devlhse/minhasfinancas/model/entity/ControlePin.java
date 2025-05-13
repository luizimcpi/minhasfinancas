package com.devlhse.minhasfinancas.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table( name = "controle_pin", schema = "public")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlePin {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column( name = "id")
    private UUID id;

    @Column( name = "email")
    private String email;

    @Column( name = "pin")
    private String pin;

    @Column( name = "data_cadastro")
    @CreationTimestamp
    private LocalDateTime dataCadastro;

    @Column( name = "data_expiracao")
    private LocalDateTime dataExpiracao;

}
