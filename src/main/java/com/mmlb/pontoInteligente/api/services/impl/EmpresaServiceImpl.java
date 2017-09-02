/**
 * 
 */
package com.mmlb.pontoInteligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private IEmpresaRepository empresaRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmlb.pontoInteligente.api.services.IEmpresaService#buscarPorCnpj(java.
	 * lang.Long)
	 */
	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("Buscando empresa por CPNJ {}", cnpj);
		return Optional.ofNullable(this.empresaRepository.findByCnpj(cnpj));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmlb.pontoInteligente.api.services.IEmpresaService#persistir(com.mmlb.
	 * pontoInteligente.api.entites.Empresa)
	 */
	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("Persisitindo a empresa {}", empresa);
		return this.empresaRepository.save(empresa);
	}

}
