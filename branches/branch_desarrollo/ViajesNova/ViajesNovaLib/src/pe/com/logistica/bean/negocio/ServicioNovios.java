/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.math.BigDecimal;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author edwreb
 *
 */
public class ServicioNovios extends BaseNegocio{

	private BaseVO tipoServicio;
	private Integer propietarioServicio;
	private String descripcionServicio;
	private int cantidad;
	private BigDecimal precioUnitario;
	private BigDecimal montoTotalTipoServicio;
	/**
	 * 
	 */
	private static final long serialVersionUID = -9198647945057223008L;

	/**
	 * 
	 */
	public ServicioNovios() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the tipoServicio
	 */
	public BaseVO getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(BaseVO tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return the propietarioServicio
	 */
	public Integer getPropietarioServicio() {
		return propietarioServicio;
	}

	/**
	 * @param propietarioServicio the propietarioServicio to set
	 */
	public void setPropietarioServicio(Integer propietarioServicio) {
		this.propietarioServicio = propietarioServicio;
	}

	/**
	 * @return the descripcionServicio
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	/**
	 * @param descripcionServicio the descripcionServicio to set
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the precioUnitario
	 */
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	/**
	 * @param precioUnitario the precioUnitario to set
	 */
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	/**
	 * @return the montoTotalTipoServicio
	 */
	public BigDecimal getMontoTotalTipoServicio() {
		
		try {
			BigDecimal cant = BigDecimal.valueOf(Double.valueOf(String.valueOf(getCantidad())));
			
			montoTotalTipoServicio = this.getPrecioUnitario().multiply(cant);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e){
			
		}
		
		return montoTotalTipoServicio;
	}

	/**
	 * @param montoTotalTipoServicio the montoTotalTipoServicio to set
	 */
	public void setMontoTotalTipoServicio(BigDecimal montoTotalTipoServicio) {
		this.montoTotalTipoServicio = montoTotalTipoServicio;
	}

}
