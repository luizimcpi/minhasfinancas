package com.devlhse.minhasfinancas.model.entity;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
