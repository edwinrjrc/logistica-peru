/**
 * 
 */
package pe.com.logistica.bean.base;

/**
 * @author Edwin
 *
 */
public class CorreoElectronico extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2802017659577078384L;


	private String direccion;
	private boolean seleccionado;


	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}


	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	/**
	 * @return the seleccionado
	 */
	public boolean isSeleccionado() {
		return seleccionado;
	}


	/**
	 * @param seleccionado the seleccionado to set
	 */
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
}
