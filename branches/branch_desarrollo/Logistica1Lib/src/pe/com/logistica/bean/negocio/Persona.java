/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.bean.base.DocumentoIdentidad;

/**
 * @author Edwin
 * 
 */
public class Persona extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DocumentoIdentidad documentoIdentidad;
	private String nombres;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String razonSocial;
	private List<Direccion> listaDirecciones;

	/**
	 * 
	 */
	public Persona() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the documentoIdentidad
	 */
	public DocumentoIdentidad getDocumentoIdentidad() {
		if (documentoIdentidad == null){
			documentoIdentidad = new DocumentoIdentidad();
		}
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

	/**
	 * @return the listaDirecciones
	 */
	public List<Direccion> getListaDirecciones() {
		if (listaDirecciones == null){
			listaDirecciones = new ArrayList<Direccion>();
		}
		return listaDirecciones;
	}

	/**
	 * @param listaDirecciones the listaDirecciones to set
	 */
	public void setListaDirecciones(List<Direccion> listaDirecciones) {
		this.listaDirecciones = listaDirecciones;
	}
	
	public String getNombreCompleto(){
		String nombreCompleto = StringUtils.trim(getNombres())+ " "+ StringUtils.trim(getApellidoPaterno())+ " "+StringUtils.trim(getApellidoMaterno());
		nombreCompleto = StringUtils.normalizeSpace(nombreCompleto);
		
		return nombreCompleto;
	}

}
