/**
 * 
 */
package com.mmlb.pontoInteligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmlb.pontoInteligente.api.entites.Funcionario;
import com.mmlb.pontoInteligente.api.repositories.IFuncionarioRepository;
import com.mmlb.pontoInteligente.api.services.IFuncionarioService;

/**
 * @author marcelolimabh
 *
 */
@Service
public class FuncionarioServiceImpl implements IFuncionarioService {
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);
	
	@Autowired
	private IFuncionarioRepository funcionarioRepository;
	

	@Override
	public Funcionario persistir(Funcionario funcionario) {
		log.info("Persisitindo o funcionario {}", funcionario);
		return this.funcionarioRepository.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscaPorId(Long id) {
		log.info("Recupera o funcionario pelo id: {}", id);
		return Optional.ofNullable(this.funcionarioRepository.findOne(id));
	}

	
	@Override
	public Optional<Funcionario> buscaPorCpf(String cpf) {
		log.info("Recupera o funcionario pelo cpf: {}", cpf);
		return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
	}

	
	@Override
	public Optional<Funcionario> buscaPorEmail(String email) {
		log.info("Recupera o funcionario pelo email: {}", email);
		return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
	}

	/**
	 * @return the funcionarioRepository
	 */
	public IFuncionarioRepository getFuncionarioRepository() {
		return funcionarioRepository;
	}

	/**
	 * @param funcionarioRepository the funcionarioRepository to set
	 */
	public void setFuncionarioRepository(IFuncionarioRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}

}
