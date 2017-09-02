/**
 * 
 */
package com.mmlb.pontoInteligente.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mmlb.pontoInteligente.api.entites.Empresa;
import com.mmlb.pontoInteligente.api.entites.Funcionario;
import com.mmlb.pontoInteligente.api.enums.PerfilEnum;
import com.mmlb.pontoInteligente.api.utils.PasswordUtils;

/**
 * @author marcelolimabh
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {
	
	
	private static final String CPF = "82629373258";
	private static final String EMAIL = "func@empresa.com";
	
	
	@Autowired
	private IFuncionarioRepository funcionarioRepository;
	
	@Autowired
	private IEmpresaRepository empresaRepository;
	
	
	@Before
	public void setUp() {
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		this.funcionarioRepository.save(obterDadosFuncionario(empresa));
	}
	
	@After
	public void tearDown() {
		this.funcionarioRepository.deleteAll();
	}
	
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		Funcionario func = this.funcionarioRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, func.getEmail());
	}
	
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		Funcionario func = this.funcionarioRepository.findByCpf(CPF);
		assertEquals(CPF, func.getCpf());
	}
	
	@Test
	public void testBuscarFuncionarioPoerEmailECpf() {
		Funcionario func = this.funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);
		assertNotNull(func);
	}
	
	@Test
	public void testBuscarFuncionarioPoerEmailECpfComEmailInvalido() {
		Funcionario func = this.funcionarioRepository.findByCpfOrEmail(CPF, "email.invalidoX@xxx.com");
		assertNotNull(func);
	}
	
	@Test
	public void testBuscarFuncionarioPoerEmailECpfComCpfInvalido() {
		Funcionario func = this.funcionarioRepository.findByCpfOrEmail("11111111111", EMAIL);
		assertNotNull(func);
	}



	private Funcionario obterDadosFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("José das Couve");
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setSenha(PasswordUtils.geraBCrypt("123456"));
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setEmpresa(empresa);
		
		return funcionario;
	}



	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa do Zé das Couve");
		empresa.setCnpj("17755383000100");
		return empresa;
	}
	
	
	
	
	
	

}
