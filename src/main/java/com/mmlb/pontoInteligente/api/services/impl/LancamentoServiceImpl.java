/**
 * 
 */
package com.mmlb.pontoInteligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mmlb.pontoInteligente.api.entites.Lancamento;
import com.mmlb.pontoInteligente.api.repositories.ILancamentoRepository;
import com.mmlb.pontoInteligente.api.services.ILancamentoService;

/**
 * @author marcelolimabh
 *
 */
@Service
public class LancamentoServiceImpl implements ILancamentoService {

	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

	@Autowired
	private ILancamentoRepository lancamentoRepository;

	@Override
	@CachePut("lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Persisitindo o lancamento {}", lancamento);
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
		log.info("Bucando lançamentos pelo id do funcionario : {}", funcionarioId);
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Override
	@Cacheable("lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Busca o lancamento do id: {}", id);
		return Optional.ofNullable(this.lancamentoRepository.findOne(id));
	}

	@Override
	public void remover(Long id) {
		log.info("Remover o lancamento  id: {}", id);
		this.lancamentoRepository.delete(id);

	}

	public ILancamentoRepository getLancamentoRepository() {
		return lancamentoRepository;
	}

	public void setLancamentoRepository(ILancamentoRepository lancamentoRepository) {
		this.lancamentoRepository = lancamentoRepository;
	}

}
