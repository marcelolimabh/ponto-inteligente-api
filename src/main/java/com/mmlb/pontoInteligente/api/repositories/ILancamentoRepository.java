/**
 * 
 */
package com.mmlb.pontoInteligente.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mmlb.pontoInteligente.api.entites.Lancamento;

/**
 * @author marcelolimabh
 *
 */
@Transactional(readOnly=true)
@NamedQueries({
	@NamedQuery(name="ILancamentoRepository.findByFuncionarioId", query="SELECT lanc FROM Lancamento lanc WHERE lanc.funcionario.id = :funcionarioId" )
})
public interface ILancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	List<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);
	
	Page<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId, Pageable pageable);

}
