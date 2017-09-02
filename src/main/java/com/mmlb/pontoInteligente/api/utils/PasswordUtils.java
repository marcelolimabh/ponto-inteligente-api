/**
 * 
 */
package com.mmlb.pontoInteligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @author marcelolimabh
 *
 */
public class PasswordUtils {
	
	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);

	public PasswordUtils() {
		
	}
	
	public static String geraBCrypt(String senha) {
		if(senha==null) {
			return senha;
		}
		log.info("Gerando hash com BCrypt");
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		return bCrypt.encode(senha);
	}
	
	
	
	

}
