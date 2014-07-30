/**
 * 
 */
package pe.com.logistica.bean.base;

/**
 * @author Edwin
 * 
 */
public class BaseVO extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nombre;

	/**
	 * 
	 */
	public BaseVO() {
		// TODO Auto-generated constructor stub
	}
	
	public BaseVO(int id) {
		this.setCodigoEntero(id);
	}
	
	public BaseVO(String id) {
		this.setCodigoCadena(id);
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
