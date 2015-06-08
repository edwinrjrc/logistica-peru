/**
 * 
 */
package pe.com.logistica.bean.reportes;

import java.util.Date;

import pe.com.logistica.bean.base.Base;

/**
 * @author Edwin
 *
 */
public class ReporteVentas extends Base {

	private Date fechaDesde;
	private Date fechaHasta;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3961646032519921135L;

	/**
	 * @return the fechaDesde
	 */
	public Date getFechaDesde() {
		return fechaDesde;
	}

	/**
	 * @param fechaDesde the fechaDesde to set
	 */
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	/**
	 * @return the fechaHasta
	 */
	public Date getFechaHasta() {
		return fechaHasta;
	}

	/**
	 * @param fechaHasta the fechaHasta to set
	 */
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

}
