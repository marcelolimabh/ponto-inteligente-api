/**
 * 
 */
package com.mmlb.pontoInteligente.api.controllers;

import java.security.NoSuchAlgorithmException;

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

import com.mmlb.pontoInteligente.api.dtos.CadastroPJDto;
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
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);

	@Autowired
	private IFuncionarioService funcionarioService;

	@Autowired
	private IEmpresaService empresaService;

	public CadastroPJController() {

	}

	@PostMapping
	public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto dto,
			BindingResult result) throws NoSuchAlgorithmException {

		log.info("Cadastrando PJ: {}", dto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();

		validarDadosExistentes(dto, result);

		Empresa empresa = this.converterDtoParaEmpresa(dto);
		Funcionario funcionario = this.converterDtoParaFuncionario(dto, empresa);

		if (result.hasErrors()) {
			log.error("Erro validando dados  de cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);

		response.setData(this.converterCadastroPJDto(funcionario));
		return ResponseEntity.ok(response);

	}

	private void validarDadosExistentes(CadastroPJDto dto, BindingResult result) {
		this.empresaService.buscarPorCnpj(dto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));
		this.funcionarioService.buscaPorCpf(dto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPJ já existe")));
		this.funcionarioService.buscaPorEmail(dto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("empresa", "Email já existe.")));

	}

	/**
	 * Converte o dados do dto de request em uma entidade empresa
	 * @param dto
	 * @return
	 */
	private Empresa converterDtoParaEmpresa(CadastroPJDto dto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(dto.getCnpj());
		empresa.setRazaoSocial(dto.getRazaoSocial());
		return empresa;
	}

	/**
	 * Converte as innformacoes do dto de request em uma entidade funcionario
	 * @param dto
	 * @param empresa
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPJDto dto, Empresa empresa)
			throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(dto.getNome());
		funcionario.setEmail(dto.getEmail());
		funcionario.setCpf(dto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.geraBCrypt(dto.getSenha()));

		return funcionario;
	}

	/**
	 * Converte os dados persistidos em um dto de response
	 * @param funcionario
	 * @return
	 */
	private CadastroPJDto converterCadastroPJDto(Funcionario funcionario) {
		CadastroPJDto dto = new CadastroPJDto();
		dto.setId(funcionario.getId());
		dto.setNome(funcionario.getNome());
		dto.setCpf(funcionario.getCpf());
		dto.setEmail(funcionario.getEmail());
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		dto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		return dto;
	}

}
