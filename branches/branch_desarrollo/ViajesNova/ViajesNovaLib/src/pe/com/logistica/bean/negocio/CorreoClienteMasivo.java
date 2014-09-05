/**
 * 
 */
package pe.com.logistica.bean.negocio;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.CorreoElectronico;

/**
 * @author Edwin
 *
 */
public class CorreoClienteMasivo extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4725071849720994246L;

	private Cliente cliente;
	private Contacto contacto;
	private CorreoElectronico correoElectronico;
	
	private boolean enviarCorreo;
	/**
	 * 
	 */
	public CorreoClienteMasivo() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		if (cliente == null){
			cliente = new Cliente();
		}
		return cliente;
	}
	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	/**
	 * @return the contacto
	 */
	public Contacto getContacto() {
		if (contacto == null){
			contacto = new Contacto();
		}
		return contacto;
	}
	/**
	 * @param contacto the contacto to set
	 */
	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}
	/**
	 * @return the correoElectronico
	 */
	public CorreoElectronico getCorreoElectronico() {
		if (correoElectronico == null){
			correoElectronico = new CorreoElectronico();
		}
		return correoElectronico;
	}
	/**
	 * @param correoElectronico the correoElectronico to set
	 */
	public void setCorreoElectronico(CorreoElectronico correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	/**
	 * @return the enviarCorreo
	 */
	public boolean isEnviarCorreo() {
		return enviarCorreo;
	}
	/**
	 * @param enviarCorreo the enviarCorreo to set
	 */
	public void setEnviarCorreo(boolean enviarCorreo) {
		this.enviarCorreo = enviarCorreo;
	}

}
