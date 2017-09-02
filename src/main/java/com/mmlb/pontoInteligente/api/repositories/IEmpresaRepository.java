/**
 * 
 */
package com.mmlb.pontoInteligente.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mmlb.pontoInteligente.api.entites.Empresa;

/**
 * @author marcelolimabh
 *
 */
public interface IEmpresaRepository extends JpaRepository<Empresa, Long> {
	
	@Transactional(readOnly = true)
	Empresa findByCnpj(String cnpj);

}
