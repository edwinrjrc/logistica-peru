/**
 * 
 */
package pe.com.logistica.web.faces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import pe.com.logistica.bean.negocio.CorreoMasivo;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "correoMasivoMBean" )
@SessionScoped()
public class CorreoMasivoMBean extends BaseMBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8169173720644254644L;
	
	private CorreoMasivo correoMasivo;

	/**
	 * 
	 */
	public CorreoMasivoMBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void nuevoEnvio(){
		
	}

	/**
	 * @return the correoMasivo
	 */
	public CorreoMasivo getCorreoMasivo() {
		return correoMasivo;
	}

	/**
	 * @param correoMasivo the correoMasivo to set
	 */
	public void setCorreoMasivo(CorreoMasivo correoMasivo) {
		this.correoMasivo = correoMasivo;
	}

}
