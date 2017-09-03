/**
 * 
 */
package com.mmlb.pontoInteligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.mmlb.pontoInteligente.api.entites.Lancamento;

/**
 * @author marcelolimabh
 *
 */
public interface ILancamentoService {

	/**
	 * Salva o lancamento na base de dados
	 * 
	 * @param lancamento
	 * @return
	 */
	Lancamento persistir(Lancamento lancamento);

	/**
	 * Obter lancamentos pelo id do funcionario
	 * 
	 * @param funcionarioId
	 * @param pageRequest
	 * @return
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);

	/**
	 * Obter lancamento pelo id do lancamento
	 * 
	 * @param id
	 * @return
	 */
	Optional<Lancamento> buscarPorId(Long id);

	/**
	 * Excluir da base de dados o registro do lancamento pelo seu ID
	 * 
	 * @param id
	 */
	void remover(Long id);

}
