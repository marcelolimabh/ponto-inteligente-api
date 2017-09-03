/**
 * 
 */
package com.mmlb.pontoInteligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmlb.pontoInteligente.api.entites.Empresa;
import com.mmlb.pontoInteligente.api.repositories.IEmpresaRepository;
import com.mmlb.pontoInteligente.api.services.IEmpresaService;

/**
 * @author marcelolimabh
 *
 */
@Service
public class EmpresaServiceImpl implements IEmpresaService {

	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	private IEmpresaRepository empresaRepository;

	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("Buscando empresa por CPNJ {}", cnpj);
		return Optional.ofNullable(this.empresaRepository.findByCnpj(cnpj));

	}

	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("Persisitindo a empresa {}", empresa);
		return this.empresaRepository.save(empresa);
	}

	/**
	 * @return the empresaRepository
	 */
	public IEmpresaRepository getEmpresaRepository() {
		return empresaRepository;
	}

	/**
	 * @param empresaRepository
	 *            the empresaRepository to set
	 */
	public void setEmpresaRepository(IEmpresaRepository empresaRepository) {
		this.empresaRepository = empresaRepository;
	}

}
