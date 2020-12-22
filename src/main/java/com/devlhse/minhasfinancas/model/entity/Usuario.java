package com.devlhse.minhasfinancas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table( name = "usuario", schema = "public")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Usuario usuario = (Usuario) o;
		return id.equals(usuario.id) && nome.equals(usuario.nome) && email.equals(usuario.email) && senha.equals(
				usuario.senha);
	}

	@Override public int hashCode() {
		return Objects.hash(id, nome, email, senha);
	}

	@Override public String toString() {
		return "Usuario{" + "id=" + id + ", nome='" + nome + '\'' + ", email='" + email + '\'' + ", senha='" + senha + '\'' + '}';
	}
}
