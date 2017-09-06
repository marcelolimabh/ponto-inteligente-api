/**
 * 
 */
package com.mmlb.pontoInteligente.api.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mmlb.pontoInteligente.api.dtos.LancamentoDto;
import com.mmlb.pontoInteligente.api.entites.Funcionario;
import com.mmlb.pontoInteligente.api.entites.Lancamento;
import com.mmlb.pontoInteligente.api.enums.TipoEnum;
import com.mmlb.pontoInteligente.api.response.Response;
import com.mmlb.pontoInteligente.api.services.IFuncionarioService;
import com.mmlb.pontoInteligente.api.services.ILancamentoService;

/**
 * @author marcelolimabh
 *
 */
@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LancamentoController.class);

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private ILancamentoService lancamentoService;

	@Autowired
	private IFuncionarioService funcionarioService;

	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	public LancamentoController() {
	}

	/**
	 * Lista os lancamentos do funcionario pelo seu ID
	 * 
	 * @param funcionarioId
	 * @param pag
	 * @param ord
	 * @param dir
	 * @return ResponseEntity<Response<Page<LancamentoDto>>>
	 */
	@GetMapping(value = "/funcionario/{funcionarioId}")
	public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
			@PathVariable("funcionarioId") Long funcionarioId, @RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		LOGGER.info("Buscando Lancamentos por ID do funcionario:  {}", funcionarioId);
		Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();

		PageRequest pageRequest = new PageRequest(pag, qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
		Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.converterLancamentoParaDto(lancamento));
		response.setData(lancamentosDto);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Recupera o lancamento pelo id
	 * 
	 * @param id
	 * @return ResponseEntity<Response<LancamentoDto>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {
		LOGGER.info("Listar o lancamento por ID: {}", id);
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

		if (!lancamento.isPresent()) {
			LOGGER.error("Lançamento não encontrado pelo id: {}", id);
			response.getErros().add("Lancamento não encontrado pelo id: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterLancamentoParaDto(lancamento.get()));

		return ResponseEntity.ok().body(response);
	}

	/**
	 * Adcionando lançamento na base de dados
	 * 
	 * @param dtoRequest
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto dtoRequest,
			BindingResult result) throws ParseException {
		LOGGER.info("Adicionando o lancamento: {}", dtoRequest.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarLancamento(dtoRequest, result);
		Lancamento lancamento = this.converterDtoParaLancamento(dtoRequest, result);

		if (result.hasErrors()) {
			LOGGER.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoParaDto(lancamento));

		return ResponseEntity.ok().body(response);
	}

	/**
	 * Atualiza os dados de um lançamento.
	 * 
	 * @param id
	 * @param lancamentoDto
	 * @return ResponseEntity<Response<Lancamento>>
	 * @throws ParseException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
		LOGGER.info("Atualizando lançamento: {}", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarLancamento(lancamentoDto, result);
		lancamentoDto.setId(Optional.of(id));
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

		if (result.hasErrors()) {
			LOGGER.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoParaDto(lancamento));
		return ResponseEntity.ok(response);
	}

	/**
	 * Remove um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Lancamento>>
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		LOGGER.info("Removendo lançamento: {}", id);
		Response<String> response = new Response<String>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

		if (!lancamento.isPresent()) {
			LOGGER.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
			response.getErros().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.lancamentoService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}

	/**
	 * Converte o dto do request para lancamento
	 * 
	 * @param lancamentoDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result)
			throws ParseException {
		Lancamento lancamento = new Lancamento();

		if (lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());
			if (lanc.isPresent()) {
				lancamento = lanc.get();
			} else {
				result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
			}
		} else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		}

		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
		lancamento.setData(this.sdf.parse(lancamentoDto.getData()));

		if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
		} else {
			result.addError(new ObjectError("tipo", "Tipo inválido."));
		}

		return lancamento;
	}

	/**
	 * Valida se existe error no dto do request
	 * 
	 * @param dtoRequest
	 * @param result
	 */
	private void validarLancamento(LancamentoDto dtoRequest, BindingResult result) {
		if (dtoRequest.getFuncionarioId() == null) {
			result.addError(new ObjectError("funcionario", "Funcionário não informado."));
			return;
		}
		LOGGER.info("Obtendo funcionario pelo ID: {}", dtoRequest.getFuncionarioId());
		Optional<Funcionario> funcionario = this.funcionarioService.buscaPorId(dtoRequest.getFuncionarioId());
		if (!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não encontrado ID inexistente."));
		}

	}

	/**
	 * Converte o lancamento para o dto de response
	 * 
	 * @param lancamento
	 * @return
	 */
	private LancamentoDto converterLancamentoParaDto(Lancamento lancamento) {
		LancamentoDto dtoResponse = new LancamentoDto();
		dtoResponse.setId(Optional.of((lancamento.getId())));
		dtoResponse.setData(sdf.format(lancamento.getData()));
		dtoResponse.setDescricao(lancamento.getDescricao());
		dtoResponse.setTipo(lancamento.getTipo().toString());
		dtoResponse.setLocalizacao(lancamento.getLocalizacao());
		dtoResponse.setFuncionarioId(lancamento.getFuncionario().getId());
		return dtoResponse;
	}

}
