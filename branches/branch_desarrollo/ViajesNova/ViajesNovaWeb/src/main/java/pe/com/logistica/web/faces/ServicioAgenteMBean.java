/**
 * 
 */
package pe.com.logistica.web.faces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import pe.com.logistica.bean.negocio.ServicioAgencia;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "servicioAgenteMBean")
@SessionScoped()
public class ServicioAgenteMBean extends BaseMBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3451688997471435575L;
	
	private ServicioAgencia servicioAgencia;

	/**
	 * 
	 */
	public ServicioAgenteMBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the servicioAgencia
	 */
	public ServicioAgencia getServicioAgencia() {
		return servicioAgencia;
	}

	/**
	 * @param servicioAgencia the servicioAgencia to set
	 */
	public void setServicioAgencia(ServicioAgencia servicioAgencia) {
		this.servicioAgencia = servicioAgencia;
	}

}
