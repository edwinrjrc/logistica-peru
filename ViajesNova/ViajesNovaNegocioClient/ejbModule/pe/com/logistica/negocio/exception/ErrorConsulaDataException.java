/**
 * 
 */
package pe.com.logistica.negocio.exception;

/**
 * @author edwreb
 *
 */
public class ErrorConsulaDataException extends LogisticaNegocioException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6953473364013298861L;

	/**
	 * 
	 */
	public ErrorConsulaDataException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ErrorConsulaDataException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ErrorConsulaDataException(String arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ErrorConsulaDataException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ErrorConsulaDataException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param codigo
	 * @param mensaje
	 * @param arg1
	 */
	public ErrorConsulaDataException(String codigo, String mensaje,
			Throwable arg1) {
		super(codigo, mensaje, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public ErrorConsulaDataException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
