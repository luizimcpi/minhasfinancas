package com.devlhse.minhasfinancas.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table( name = "usuario", schema = "public")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

	@Id
	@Column( name = "id")
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;

	@Column( name = "nome")
	private String nome;

	@Column( name = "email")
	private String email;

	@Column( name = "senha")
	private String senha;

	@Column( name = "data_cadastro")
	@CreationTimestamp
	private LocalDateTime dataCadastro;
}
