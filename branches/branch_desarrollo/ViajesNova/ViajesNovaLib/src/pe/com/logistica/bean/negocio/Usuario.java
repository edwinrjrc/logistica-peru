/**
 * 
 */
package pe.com.logistica.bean.negocio;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.base.Persona;

/**
 * @author Edwin
 * 
 */
public class Usuario extends Persona {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String usuario;
	private String credencial;
	private String credencialNueva;
	private BaseVO rol;
	private boolean encontrado;
	
	/**
	 * 
	 */
	public Usuario() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the credencial
	 */
	public String getCredencial() {
		return credencial;
	}

	/**
	 * @param credencial
	 *            the credencial to set
	 */
	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}

	/**
	 * @return the rol
	 */
	public BaseVO getRol() {
		if (rol == null) {
			rol = new BaseVO();
		}
		return rol;
	}

	/**
	 * @param rol
	 *            the rol to set
	 */
	public void setRol(BaseVO rol) {
		this.rol = rol;
	}

	/**
	 * @return the encontrado
	 */
	public boolean isEncontrado() {
		return encontrado;
	}

	/**
	 * @param encontrado the encontrado to set
	 */
	public void setEncontrado(boolean encontrado) {
		this.encontrado = encontrado;
	}

	/**
	 * @return the credencialNueva
	 */
	public String getCredencialNueva() {
		return credencialNueva;
	}

	/**
	 * @param credencialNueva the credencialNueva to set
	 */
	public void setCredencialNueva(String credencialNueva) {
		this.credencialNueva = credencialNueva;
	}

}
