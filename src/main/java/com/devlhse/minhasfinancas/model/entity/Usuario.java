package com.devlhse.minhasfinancas.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@JsonIgnore
	@Column( name = "nome")
	private String nome;

	@JsonIgnore
	@Column( name = "email")
	private String email;

	@JsonIgnore
	@Column( name = "senha")
	private String senha;

	@Column( name = "ativo")
	@JsonIgnore
	private boolean ativo;

	@JsonIgnore
	@Column( name = "data_cadastro")
	@CreationTimestamp
	private LocalDateTime dataCadastro;
}
