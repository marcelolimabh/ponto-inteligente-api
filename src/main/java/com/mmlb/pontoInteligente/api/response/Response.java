/**
 * 
 */
package com.mmlb.pontoInteligente.api.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author marcelolimabh
 *
 */
public class Response<T> {
	
	private T data;
	
	private List<String> erros;
	
	public Response() {
		
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return the erros
	 */
	public List<String> getErros() {
		if(erros==null) {
			erros = new ArrayList<String>();
		}
		return erros;
	}

	/**
	 * @param erros the erros to set
	 */
	public void setErros(List<String> erros) {
		this.erros = erros;
	}
	
	

}
