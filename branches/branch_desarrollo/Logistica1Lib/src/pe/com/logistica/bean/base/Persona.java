package pe.com.logistica.bean.base;

/**
 * @author Edwin
 * @version 1.0
 * @created 14-dic-2013 01:14:34 p.m.
 */
public class Persona extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4643534946632179203L;
	private String apellidoMaterno;
	private String apellidoPaterno;
	private Direccion direccion;
	private DocumentoIdentidad documentoIdentidad;
	private BaseVO estadoCivil;
	private BaseVO genero;
	private String nombres;
	private String razonSocial;

	public Persona() {

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * @return the apellidoMaterno
	 */
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	/**
	 * @param apellidoMaterno
	 *            the apellidoMaterno to set
	 */
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	/**
	 * @return the apellidoPaterno
	 */
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	/**
	 * @param apellidoPaterno
	 *            the apellidoPaterno to set
	 */
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	/**
	 * @return the direccion
	 */
	public Direccion getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            the direccion to set
	 */
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the documentoIdentidad
	 */
	public DocumentoIdentidad getDocumentoIdentidad() {
		return documentoIdentidad;
	}

	/**
	 * @param documentoIdentidad
	 *            the documentoIdentidad to set
	 */
	public void setDocumentoIdentidad(DocumentoIdentidad documentoIdentidad) {
		this.documentoIdentidad = documentoIdentidad;
	}

	/**
	 * @return the estadoCivil
	 */
	public BaseVO getEstadoCivil() {
		return estadoCivil;
	}

	/**
	 * @param estadoCivil
	 *            the estadoCivil to set
	 */
	public void setEstadoCivil(BaseVO estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	/**
	 * @return the genero
	 */
	public BaseVO getGenero() {
		return genero;
	}

	/**
	 * @param genero
	 *            the genero to set
	 */
	public void setGenero(BaseVO genero) {
		this.genero = genero;
	}

	/**
	 * @return the nombres
	 */
	public String getNombres() {
		return nombres;
	}

	/**
	 * @param nombres
	 *            the nombres to set
	 */
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	/**
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial
	 *            the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

}