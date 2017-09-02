/**
 * 
 */
package com.mmlb.pontoInteligente.api.services;

import java.util.Optional;

import com.mmlb.pontoInteligente.api.entites.Empresa;

/**
 * @author marcelolimabh
 *
 */
public interface IEmpresaService {
	
	
	/**
	 * Busca lista de empresas por CNPJ
	 * @param cnpj
	 * @return
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	
	/**
	 * Gravar empresa
	 * @param empresa
	 * @return
	 */
	Empresa persistir(Empresa empresa);

}
