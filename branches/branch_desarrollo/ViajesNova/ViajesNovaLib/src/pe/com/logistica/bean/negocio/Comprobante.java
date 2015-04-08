/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 *
 */
public class Comprobante extends BaseNegocio {

	private BaseVO tipoComprobante;
	private String numeroComprobante;
	private Cliente titular;
	private Date fechaComprobante;
	private BigDecimal totalIGV;
	private BigDecimal totalComprobante;
	private Integer idServicio;
	
	private List<DetalleComprobante> detalleComprobante;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4234081010839284200L;

	/**
	 * 
	 */
	public Comprobante() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the tipoComprobante
	 */
	public BaseVO getTipoComprobante() {
		return tipoComprobante;
	}

	/**
	 * @param tipoComprobante the tipoComprobante to set
	 */
	public void setTipoComprobante(BaseVO tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	/**
	 * @return the numeroComprobante
	 */
	public String getNumeroComprobante() {
		return numeroComprobante;
	}

	/**
	 * @param numeroComprobante the numeroComprobante to set
	 */
	public void setNumeroComprobante(String numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}

	/**
	 * @return the titular
	 */
	public Cliente getTitular() {
		return titular;
	}

	/**
	 * @param titular the titular to set
	 */
	public void setTitular(Cliente titular) {
		this.titular = titular;
	}

	/**
	 * @return the fechaComprobante
	 */
	public Date getFechaComprobante() {
		return fechaComprobante;
	}

	/**
	 * @param fechaComprobante the fechaComprobante to set
	 */
	public void setFechaComprobante(Date fechaComprobante) {
		this.fechaComprobante = fechaComprobante;
	}

	/**
	 * @return the totalIGV
	 */
	public BigDecimal getTotalIGV() {
		return totalIGV;
	}

	/**
	 * @param totalIGV the totalIGV to set
	 */
	public void setTotalIGV(BigDecimal totalIGV) {
		this.totalIGV = totalIGV;
	}

	/**
	 * @return the totalComprobante
	 */
	public BigDecimal getTotalComprobante() {
		return totalComprobante;
	}

	/**
	 * @param totalComprobante the totalComprobante to set
	 */
	public void setTotalComprobante(BigDecimal totalComprobante) {
		this.totalComprobante = totalComprobante;
	}

	/**
	 * @return the detalleComprobante
	 */
	public List<DetalleComprobante> getDetalleComprobante() {
		if (detalleComprobante == null){
			detalleComprobante = new ArrayList<DetalleComprobante>();
		}
		return detalleComprobante;
	}

	/**
	 * @param detalleComprobante the detalleComprobante to set
	 */
	public void setDetalleComprobante(List<DetalleComprobante> detalleComprobante) {
		this.detalleComprobante = detalleComprobante;
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

}
