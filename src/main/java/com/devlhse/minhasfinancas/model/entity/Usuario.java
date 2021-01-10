package com.devlhse.minhasfinancas.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

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

	@Column( name = "senha")
	private String senha;

	@Column( name = "ativo")
	@JsonIgnore
	private boolean ativo;

	@Column( name = "data_cadastro")
	@CreationTimestamp
	private LocalDateTime dataCadastro;
}
