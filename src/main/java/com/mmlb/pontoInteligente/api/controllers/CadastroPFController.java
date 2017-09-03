/**
 * 
 */
package com.mmlb.pontoInteligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmlb.pontoInteligente.api.dtos.CadastroPFDto;
import com.mmlb.pontoInteligente.api.entites.Empresa;
import com.mmlb.pontoInteligente.api.entites.Funcionario;
import com.mmlb.pontoInteligente.api.enums.PerfilEnum;
import com.mmlb.pontoInteligente.api.response.Response;
import com.mmlb.pontoInteligente.api.services.IEmpresaService;
import com.mmlb.pontoInteligente.api.services.IFuncionarioService;
import com.mmlb.pontoInteligente.api.utils.PasswordUtils;

/**
 * @author marcelolimabh
 *
 */
@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins="*")
public class CadastroPFController {
	
	private final static  Logger log = LoggerFactory.getLogger(CadastroPFController.class);
	
	@Autowired
	private IEmpresaService empresaService;
	
	@Autowired
	private IFuncionarioService funcionarioService;
	
	public CadastroPFController() {
		
	}
	
	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto dtoRequest, BindingResult result) throws NoSuchAlgorithmException{
		log.info("Cadastrando PF {}", dtoRequest.toString());
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();
		
		validarDadosExistentes(dtoRequest, result);
		
		Funcionario funcionario = this.converterDtoParaFuncionario(dtoRequest, result);

		//Validação dos dados
		if(result.hasErrors()) {
			log.error("Erro validadno dados cadastro PF: {}",result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(dtoRequest.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterFuncionarioParaDto(funcionario));
		
		return ResponseEntity.ok(response);
	}

	


	private void validarDadosExistentes(CadastroPFDto dto, BindingResult result) {
		Optional<Empresa>empresa = this.empresaService.buscarPorCnpj(dto.getCnpj());
		if(!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}
		this.funcionarioService.buscaPorCpf(dto.getCpf()).ifPresent(func -> result.addError(new ObjectError("funcionario", "Cpf já existente.")));
		this.funcionarioService.buscaPorEmail(dto.getEmail()).ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já cadastrado.")));
		
	}

	private Funcionario converterDtoParaFuncionario(CadastroPFDto dto, BindingResult result) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(dto.getNome());
		funcionario.setCpf(dto.getCpf());
		funcionario.setEmail(dto.getEmail());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.geraBCrypt(dto.getSenha()));
		dto.getQtdHorasAlmoco().ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		dto.getQtdHorasTrabalhoDia().ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		dto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		
		return funcionario;
	}
	
	private CadastroPFDto converterFuncionarioParaDto(Funcionario funcionario) {
		CadastroPFDto dtoResponse = new CadastroPFDto();
		dtoResponse.setId(funcionario.getId());
		dtoResponse.setCnpj(funcionario.getEmpresa().getCnpj());
		dtoResponse.setEmail(funcionario.getEmail());
		dtoResponse.setNome(funcionario.getNome());
		dtoResponse.setCpf(funcionario.getCpf());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		funcionario.getValorHoraOpt().ifPresent(valorHora -> funcionario.setValorHora(valorHora));
		
		return dtoResponse;
	}

	
}
