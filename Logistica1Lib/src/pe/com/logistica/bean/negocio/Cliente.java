package pe.com.logistica.bean.negocio;

/**
 * @author Edwin
 * @version 1.0
 * @created 14-dic-2013 01:14:34 p.m.
 */
public class Cliente extends Persona {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6169046402465472276L;
	private String correoElectronico;

	public Cliente() {

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * @return the correoElectronico
	 */
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	/**
	 * @param correoElectronico
	 *            the correoElectronico to set
	 */
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

}