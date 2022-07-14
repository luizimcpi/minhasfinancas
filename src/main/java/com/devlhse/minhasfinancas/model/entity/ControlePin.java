package com.devlhse.minhasfinancas.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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
