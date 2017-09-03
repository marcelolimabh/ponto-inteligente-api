/**
 * 
 */
package com.mmlb.pontoInteligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mmlb.pontoInteligente.api.entites.Funcionario;
import com.mmlb.pontoInteligente.api.repositories.IFuncionarioRepository;
import com.mmlb.pontoInteligente.api.services.impl.FuncionarioServiceImpl;

/**
 * @author marcelolimabh
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {
	
	@MockBean
	private IFuncionarioRepository funcionarioRepository;
	
	@Autowired
	private FuncionarioServiceImpl funcionarioService;
	
	@Before
	public void setUp() throws Exception {
		
		BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findOne(Mockito.anyLong())).willReturn(new Funcionario());
		
		this.funcionarioService.setFuncionarioRepository(this.funcionarioRepository);
		
	}
	
	
	@Test
	public void testPersistirFuncionario() {
		Funcionario funcionario = this.funcionarioService.persistir(new Funcionario());
		assertNotNull(funcionario);
		
		
	}
	
	@Test
	public void testBuscarFuncionarioPorId() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscaPorId(1L);
		
		assertTrue(funcionario.isPresent());
	}
	
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscaPorCpf("11111111111");
		
		assertTrue(funcionario.isPresent());
	}
	
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscaPorEmail("email@teste.com");
		
		assertTrue(funcionario.isPresent());
	}
	
	

}
