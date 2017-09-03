/**
 * 
 */
package com.mmlb.pontoInteligente.api.dtos;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

/**
 * @author marcelolimabh
 *
 */
public class CadastroPJDto {
	
	
	private Long id;
	private String nome;
	private String email;
	private String senha;
	private String cpf;
	private String razaoSocial;
	private String cnpj;
	/**
	 * @return the id
	 */
	
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the nome
	 */
	
	@NotEmpty(message="Nome não pode ser vazio.")
	@Length(min=3, max=200, message="Nome deve conter entre 3 e 200 caracteres.")
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the email
	 */
	@NotEmpty(message="Email não pode ser vazio.")
	@Length(min=5, max=200, message="O email deve conter enttre 5 e 200 caracteres.")
	@Email(message="Email inválido.")
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the senha
	 */
	@NotEmpty(message="Senha não pode ser vazia.")
	public String getSenha() {
		return senha;
	}
	/**
	 * @param senha the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}
	/**
	 * @return the cpf
	 */
	@NotEmpty(message="CPF não pode ser vazio.")
	@CPF(message="CPF inválido.")
	public String getCpf() {
		return cpf;
	}
	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	/**
	 * @return the razaoSocial
	 */
	@NotEmpty(message="Razão Social não pode estar vazia")
	public String getRazaoSocial() {
		return razaoSocial;
	}
	/**
	 * @param razaoSocial the razaoSocial to set
	 */
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	/**
	 * @return the cnpj
	 */
	@NotEmpty(message="CNPJ não pode estar vazio")
	@CNPJ(message="CNPJ inválido")
	public String getCnpj() {
		return cnpj;
	}
	/**
	 * @param cnpj the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	
	@Override
	public String toString() {
		return "CadastroPJDto [" + (id != null ? "id=" + id + ", " : "") + (nome != null ? "nome=" + nome + ", " : "")
				+ (email != null ? "email=" + email + ", " : "") + (senha != null ? "senha=" + senha + ", " : "")
				+ (cpf != null ? "cpf=" + cpf + ", " : "")
				+ (razaoSocial != null ? "razaoSocial=" + razaoSocial + ", " : "")
				+ (cnpj != null ? "cnpj=" + cnpj : "") + "]";
	}
	
	
	
	
	
	
	
	
	

}
