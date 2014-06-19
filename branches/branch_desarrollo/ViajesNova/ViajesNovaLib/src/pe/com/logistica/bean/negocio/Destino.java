/**
 * 
 */
package pe.com.logistica.bean.negocio;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 *
 */
public class Destino extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -957344942796822259L;
	
	
	private Pais pais;
	private String descripcion;
	private BaseVO tipoDestino;
	private String codigoIATA;
	

	/**
	 * 
	 */
	public Destino() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the tipoDestino
	 */
	public BaseVO getTipoDestino() {
		if (tipoDestino == null){
			tipoDestino = new BaseVO();
		}
		return tipoDestino;
	}

	/**
	 * @param tipoDestino the tipoDestino to set
	 */
	public void setTipoDestino(BaseVO tipoDestino) {
		this.tipoDestino = tipoDestino;
	}

	/**
	 * @return the pais
	 */
	public Pais getPais() {
		if (pais == null){
			pais = new Pais();
		}
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(Pais pais) {
		this.pais = pais;
	}

	/**
	 * @return the codigoIATA
	 */
	public String getCodigoIATA() {
		return codigoIATA;
	}

	/**
	 * @param codigoIATA the codigoIATA to set
	 */
	public void setCodigoIATA(String codigoIATA) {
		this.codigoIATA = codigoIATA;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
