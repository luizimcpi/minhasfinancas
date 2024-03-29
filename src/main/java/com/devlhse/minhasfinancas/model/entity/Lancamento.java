package com.devlhse.minhasfinancas.model.entity;

import com.devlhse.minhasfinancas.model.enums.StatusLancamento;
import com.devlhse.minhasfinancas.model.enums.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table( name = "lancamento", schema = "public")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column( name = "id")
	private UUID id;

	@Column( name = "descricao")
	private String descricao;

	@Column( name = "mes")
	private Integer mes;

	@Column( name = "ano")
	private Integer ano;

	@ManyToOne
	@JoinColumn( name = "id_usuario")
	private Usuario usuario;

	@Column( name = "valor")
	private BigDecimal valor;

	@Column( name = "tipo")
	@Enumerated( value = EnumType.STRING)
	private TipoLancamento tipo;

	@Column( name = "status")
	@Enumerated( value = EnumType.STRING)
	private StatusLancamento status;

	@Column( name = "data_cadastro")
	@CreationTimestamp
	private LocalDateTime dataCadastro;

	@Column( name = "data_alteracao")
	@UpdateTimestamp
	private LocalDateTime dataAlteracao;
}
