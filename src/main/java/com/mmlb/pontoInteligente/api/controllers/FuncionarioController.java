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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmlb.pontoInteligente.api.dtos.FuncionarioDto;
import com.mmlb.pontoInteligente.api.entites.Funcionario;
import com.mmlb.pontoInteligente.api.response.Response;
import com.mmlb.pontoInteligente.api.services.IFuncionarioService;
import com.mmlb.pontoInteligente.api.utils.PasswordUtils;

/**
 * @author marcelolimabh
 *
 */
@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);

	@Autowired
	private IFuncionarioService funcionarioService;

	public FuncionarioController() {

	}

	/**
	 * Atualiza os dados de um funcionario
	 * 
	 * @param id
	 * @param funcionarioDto
	 * @param result
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody FuncionarioDto funcionarioDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando funcionario: {}", funcionarioDto.toString());

		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		Optional<Funcionario> funcionario = this.funcionarioService.buscaPorId(id);
		if (!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não encontrado."));
		}

		this.atualizarDadosFuncionario(funcionario.get(), funcionarioDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando funcionario: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.funcionarioService.persistir(funcionario.get());
		response.setData(this.converterFuncionarioParaDto(funcionario.get()));

		return ResponseEntity.ok().body(response);
	}

	private FuncionarioDto converterFuncionarioParaDto(Funcionario funcionario) {
		FuncionarioDto responseDto = new FuncionarioDto();
		responseDto.setId(funcionario.getId());
		responseDto.setNome(funcionario.getNome());
		responseDto.setEmail(funcionario.getEmail());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(
				qtdHorasAlmoco -> responseDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(qtdHorasTrabalhoDia -> responseDto
				.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabalhoDia))));
		funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> responseDto.setValorHora(Optional.of(valorHora.toString())));

		return responseDto;
	}

	/**
	 * Atualiza os dados do funcionario com base nos dados encontrados no DTO
	 * 
	 * @param funcionario
	 * @param funcionarioDto
	 * @param result
	 */
	private void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult result)
			throws NoSuchAlgorithmException {
		funcionario.setNome(funcionarioDto.getNome());

		if (!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
			this.funcionarioService.buscaPorEmail(funcionarioDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já cadastrado.")));
			funcionario.setEmail(funcionarioDto.getEmail());
		}

		funcionario.setQtdHorasAlmoco(null);
		funcionarioDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));

		funcionario.setQtdHorasTrabalhoDia(null);
		funcionarioDto.getQtdHorasTrabalhoDia().ifPresent(
				qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));

		funcionario.setValorHora(null);
		funcionarioDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

		if (funcionarioDto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.geraBCrypt(funcionarioDto.getSenha().get()));
		}

	}

}
