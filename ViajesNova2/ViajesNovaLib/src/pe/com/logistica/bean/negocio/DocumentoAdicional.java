/**
 * 
 */
package pe.com.logistica.bean.negocio;

import pe.com.logistica.bean.base.BaseNegocio;

/**
 * @author EDWREB
 *
 */
public class DocumentoAdicional extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nombreDocumento;
	private ArchivoAdjunto archivo;
	
	
	/**
	 * @return the nombreDocumento
	 */
	public String getNombreDocumento() {
		return nombreDocumento;
	}
	/**
	 * @param nombreDocumento the nombreDocumento to set
	 */
	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}
	/**
	 * @return the archivo
	 */
	public ArchivoAdjunto getArchivo() {
		if (archivo == null){
			archivo = new ArchivoAdjunto();
		}
		return archivo;
	}
	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(ArchivoAdjunto archivo) {
		this.archivo = archivo;
	}

}
