package com.devlhse.minhasfinancas.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table( name = "usuario", schema = "public")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column( name = "id")
	private UUID id;

	@Column( name = "nome")
	private String nome;

	@Column( name = "email")
	private String email;

	@JsonIgnore
	@Column( name = "senha")
	private String senha;

	@Column( name = "ativo")
	@JsonIgnore
	private boolean ativo;

	@Column( name = "valido")
	@JsonIgnore
	private boolean valido;

	@JsonIgnore
	@Column( name = "data_cadastro")
	@CreationTimestamp
	private LocalDateTime dataCadastro;

	@Column( name = "data_alteracao")
	@UpdateTimestamp
	private LocalDateTime dataAlteracao;
}
