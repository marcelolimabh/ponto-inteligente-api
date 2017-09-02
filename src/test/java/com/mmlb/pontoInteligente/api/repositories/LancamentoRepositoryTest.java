/**
 * 
 */
package com.mmlb.pontoInteligente.api.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mmlb.pontoInteligente.api.entites.Empresa;
import com.mmlb.pontoInteligente.api.entites.Funcionario;
import com.mmlb.pontoInteligente.api.entites.Lancamento;
import com.mmlb.pontoInteligente.api.enums.PerfilEnum;
import com.mmlb.pontoInteligente.api.enums.TipoEnum;
import com.mmlb.pontoInteligente.api.utils.PasswordUtils;

/**
 * @author marcelolimabh
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	private static final String CPF = "30365993638";
	private static final String CNPJ = "74628707000183";
	private static final String EMAIL = "email@teste.com";

	@Autowired
	private IEmpresaRepository empresaRepository;

	@Autowired
	private IFuncionarioRepository funcionarioRepository;

	@Autowired
	private ILancamentoRepository lancamentoRepository;

	private Long funcionarioId;

	@Before
	public void setUp() {
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());

		Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));

		this.funcionarioId = funcionario.getId();


		this.lancamentoRepository.save(obterDadosLancamento(funcionario));
		this.lancamentoRepository.save(obterDadosLancamento(funcionario));

	}

	@After
	public void tearDown() {
		this.lancamentoRepository.deleteAll();
	}

	@Test
	public void testBuscarLancamentosPorFuncionarioId() {
		List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(this.funcionarioId);
		assertEquals(2, lancamentos.size());
	}

	@Test
	public void testBuscarLancamentosPorFuncionarioIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);

		assertEquals(2, lancamentos.getTotalElements());
	}

	private Lancamento obterDadosLancamento(Funcionario funcionario) {

		Lancamento lancamento = new Lancamento();
		lancamento.setData(new Date());
		lancamento.setFuncionario(funcionario);
		lancamento.setDescricao("Descricao do lancamento: Almoço");
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);

		return lancamento;
	}

	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa Teste 01");
		empresa.setCnpj(CNPJ);
		return empresa;
	}

	private Funcionario obterDadosFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Fulano das Couve");
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setSenha(PasswordUtils.geraBCrypt("123456"));
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setEmpresa(empresa);

		return funcionario;
	}

}
