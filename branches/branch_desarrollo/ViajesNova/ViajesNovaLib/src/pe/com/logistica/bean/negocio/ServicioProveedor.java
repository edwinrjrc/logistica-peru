/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.math.BigDecimal;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 *
 */
public class ServicioProveedor extends BaseNegocio {

	private BaseVO tipoServicio;
	private BigDecimal porcentajeComision;
	private BigDecimal porcentajeFee;
	private String nombreProveedor;
	private Proveedor proveedor;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7190639530649610005L;

	/**
	 * 
	 */
	public ServicioProveedor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the tipoServicio
	 */
	public BaseVO getTipoServicio() {
		if (tipoServicio == null){
			tipoServicio = new BaseVO();
		}
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(BaseVO tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return the porcentajeComision
	 */
	public BigDecimal getPorcentajeComision() {
		return porcentajeComision;
	}

	/**
	 * @param porcentajeComision the porcentajeComision to set
	 */
	public void setPorcentajeComision(BigDecimal porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}

	/**
	 * @return the porcentajeFee
	 */
	public BigDecimal getPorcentajeFee() {
		return porcentajeFee;
	}

	/**
	 * @param porcentajeFee the porcentajeFee to set
	 */
	public void setPorcentajeFee(BigDecimal porcentajeFee) {
		this.porcentajeFee = porcentajeFee;
	}

	/**
	 * @return the nombreProveedor
	 */
	public String getNombreProveedor() {
		return nombreProveedor;
	}

	/**
	 * @param nombreProveedor the nombreProveedor to set
	 */
	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	/**
	 * @return the proveedor
	 */
	public Proveedor getProveedor() {
		if (proveedor == null){
			proveedor = new Proveedor();
		}
		return proveedor;
	}

	/**
	 * @param proveedor the proveedor to set
	 */
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

}
