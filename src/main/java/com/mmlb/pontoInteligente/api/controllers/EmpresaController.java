/**
 * 
 */
package com.mmlb.pontoInteligente.api.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmlb.pontoInteligente.api.dtos.EmpresaDto;
import com.mmlb.pontoInteligente.api.entites.Empresa;
import com.mmlb.pontoInteligente.api.response.Response;
import com.mmlb.pontoInteligente.api.services.IEmpresaService;

/**
 * @author marcelolimabh
 *
 */
@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins="*")
public class EmpresaController {
	
	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class); 
	@Autowired
	private IEmpresaService empresaService;
	
	public EmpresaController() {
		
	}
	
	@GetMapping(value="/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(@PathVariable("cnpj")String cnpj){
		log.info("Buscar empresa por CNPJ: {}", cnpj);
		Response<EmpresaDto> response = new Response<EmpresaDto>();
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cnpj);
		
		if(!empresa.isPresent()) {
			log.error("Empresa não encontrada pelo CNPJ: {}", cnpj);
			response.getErros().add("Empresa não encontrada para o CNPJ: " + cnpj);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.converterEmpresaParaDto(empresa.get()));
		
		return ResponseEntity.ok(response);
	}

	private EmpresaDto converterEmpresaParaDto(Empresa empresa) {
		EmpresaDto dtoResponse = new EmpresaDto();
		dtoResponse.setId(empresa.getId());
		dtoResponse.setRazaoSocial(empresa.getRazaoSocial());
		dtoResponse.setCnpj(empresa.getCnpj());
		return dtoResponse;
	}

}
