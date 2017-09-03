/**
 * 
 */
package com.mmlb.pontoInteligente.api.services;

import java.util.Optional;

import com.mmlb.pontoInteligente.api.entites.Funcionario;

/**
 * @author marcelolimabh
 *
 */
public interface IFuncionarioService {

	/**
	 * Grava o funcionario
	 * 
	 * @param funcionario
	 * @return
	 */
	Funcionario persistir(Funcionario funcionario);

	/**
	 * Recupera o funcionario pelo id informado
	 * @param id
	 * @return
	 */
	Optional<Funcionario> buscaPorId(Long id);
	
	/**
	 * Recupera o funcionario pelo Cpf informado
	 * @param cpf
	 * @return
	 */
	Optional<Funcionario> buscaPorCpf(String  cpf);
	
	/**
	 * Recupera o funcionario pelo email informado
	 * @param email
	 * @return
	 */
	
	Optional<Funcionario> buscaPorEmail(String email);

}
