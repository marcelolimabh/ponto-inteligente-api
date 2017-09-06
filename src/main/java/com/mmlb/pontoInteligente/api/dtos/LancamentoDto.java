/**
 * 
 */
package com.mmlb.pontoInteligente.api.dtos;

import java.util.Optional;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author marcelolimabh
 *
 */
public class LancamentoDto {

	private Optional<Long> id = Optional.empty();
	private String data;
	private String tipo;
	private String descricao;
	private String localizacao;
	private Long funcionarioId;

	/**
	 * @return the id
	 */
	public Optional<Long> getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Optional<Long> id) {
		this.id = id;
	}

	/**
	 * @return the data
	 */
	@NotEmpty(message = "Data não pode ser vazia.")
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the localizacao
	 */
	public String getLocalizacao() {
		return localizacao;
	}

	/**
	 * @param localizacao
	 *            the localizacao to set
	 */
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	/**
	 * @return the funcionalId
	 */
	public Long getFuncionarioId() {
		return funcionarioId;
	}

	/**
	 * @param funcionalId
	 *            the funcionalId to set
	 */
	public void setFuncionarioId(Long funcionalId) {
		this.funcionarioId = funcionalId;
	}

	public LancamentoDto() {

	}

	@Override
	public String toString() {
		return "LancamentoDto [" + (id != null ? "id=" + id + ", " : "") + (data != null ? "data=" + data + ", " : "")
				+ (tipo != null ? "tipo=" + tipo + ", " : "")
				+ (descricao != null ? "descricao=" + descricao + ", " : "")
				+ (localizacao != null ? "localizacao=" + localizacao + ", " : "")
				+ (funcionarioId != null ? "funcionalId=" + funcionarioId : "") + "]";
	}

}
