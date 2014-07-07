/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.util.List;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 *
 */
public class CorreoMasivo extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4117981273205473003L;
	
	
	private String cuentaEnvio;
	private String asunto;
	private BaseVO plantillaCorreo;
	private String contenidoCorreo;
	
	private List<Cliente> listaClientes;

	/**
	 * 
	 */
	public CorreoMasivo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the cuentaEnvio
	 */
	public String getCuentaEnvio() {
		return cuentaEnvio;
	}

	/**
	 * @param cuentaEnvio the cuentaEnvio to set
	 */
	public void setCuentaEnvio(String cuentaEnvio) {
		this.cuentaEnvio = cuentaEnvio;
	}

	/**
	 * @return the asunto
	 */
	public String getAsunto() {
		return asunto;
	}

	/**
	 * @param asunto the asunto to set
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	/**
	 * @return the plantillaCorreo
	 */
	public BaseVO getPlantillaCorreo() {
		return plantillaCorreo;
	}

	/**
	 * @param plantillaCorreo the plantillaCorreo to set
	 */
	public void setPlantillaCorreo(BaseVO plantillaCorreo) {
		this.plantillaCorreo = plantillaCorreo;
	}

	/**
	 * @return the contenidoCorreo
	 */
	public String getContenidoCorreo() {
		return contenidoCorreo;
	}

	/**
	 * @param contenidoCorreo the contenidoCorreo to set
	 */
	public void setContenidoCorreo(String contenidoCorreo) {
		this.contenidoCorreo = contenidoCorreo;
	}

	/**
	 * @return the listaClientes
	 */
	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	/**
	 * @param listaClientes the listaClientes to set
	 */
	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

}
