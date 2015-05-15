/**
 * 
 */
package pe.com.logistica.bean.negocio;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author EDWREB
 *
 */
public class DocumentoAdicional extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer idServicio;
	private BaseVO documento;
	private ArchivoAdjunto archivo;
	
	
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
	/**
	 * @return the idServicio
	 */
	public Integer getIdServicio() {
		return idServicio;
	}
	/**
	 * @param idServicio the idServicio to set
	 */
	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}
	/**
	 * @return the documento
	 */
	public BaseVO getDocumento() {
		if (documento == null){
			documento = new BaseVO();
		}
		return documento;
	}
	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(BaseVO documento) {
		this.documento = documento;
	}

}
