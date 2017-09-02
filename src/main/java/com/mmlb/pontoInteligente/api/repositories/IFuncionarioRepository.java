/**
 * 
 */
package com.mmlb.pontoInteligente.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mmlb.pontoInteligente.api.entites.Funcionario;

/**
 * @author marcelolimabh
 *
 */
@Transactional(readOnly=true)
public interface IFuncionarioRepository extends JpaRepository<Funcionario,Long> {
	
	Funcionario findByCpf(String cpf);
	
	Funcionario findByEmail(String email);
	
	Funcionario findByCpfOrEmail(String cpf, String email);

}
