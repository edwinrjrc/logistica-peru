/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.math.BigDecimal;
import java.util.Date;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author edwreb
 *
 */
public class DetalleServicioAgencia extends BaseNegocio {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5173839888704484950L;
	
	private BaseVO tipoServicio;
	private String descripcionServicio;
	private BaseVO destino;
	private int dias;
	private int noches;
	private Date fechaServicio;
	private Date fechaIda;
	private Date fechaRegreso;
	private int cantidad;
	private BigDecimal precioUnitario;
	private BigDecimal montoComision;
	private ServicioProveedor servicioProveedor;

	/**
	 * 
	 */
	public DetalleServicioAgencia() {
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
	 * @return the destino
	 */
	public BaseVO getDestino() {
		if (destino == null){
			destino = new BaseVO();
		}
		return destino;
	}

	/**
	 * @param destino the destino to set
	 */
	public void setDestino(BaseVO destino) {
		this.destino = destino;
	}

	/**
	 * @return the dias
	 */
	public int getDias() {
		return dias;
	}

	/**
	 * @param dias the dias to set
	 */
	public void setDias(int dias) {
		this.dias = dias;
	}

	/**
	 * @return the noches
	 */
	public int getNoches() {
		return noches;
	}

	/**
	 * @param noches the noches to set
	 */
	public void setNoches(int noches) {
		this.noches = noches;
	}

	/**
	 * @return the fechaIda
	 */
	public Date getFechaIda() {
		return fechaIda;
	}

	/**
	 * @param fechaIda the fechaIda to set
	 */
	public void setFechaIda(Date fechaIda) {
		this.fechaIda = fechaIda;
	}

	/**
	 * @return the fechaRegreso
	 */
	public Date getFechaRegreso() {
		return fechaRegreso;
	}

	/**
	 * @param fechaRegreso the fechaRegreso to set
	 */
	public void setFechaRegreso(Date fechaRegreso) {
		this.fechaRegreso = fechaRegreso;
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
	
	public BigDecimal getTotalServicio(){
		BigDecimal cantidadDecimal = UtilParse.parseIntABigDecimal(cantidad);
		BigDecimal total = this.precioUnitario.multiply(cantidadDecimal);
		return total;
	}

	/**
	 * @return the fechaServicio
	 */
	public Date getFechaServicio() {
		return fechaServicio;
	}

	/**
	 * @param fechaServicio the fechaServicio to set
	 */
	public void setFechaServicio(Date fechaServicio) {
		this.fechaServicio = fechaServicio;
	}

	/**
	 * @return the servicioProveedor
	 */
	public ServicioProveedor getServicioProveedor() {
		if (servicioProveedor == null){
			servicioProveedor = new ServicioProveedor();
		}
		return servicioProveedor;
	}

	/**
	 * @param servicioProveedor the servicioProveedor to set
	 */
	public void setServicioProveedor(ServicioProveedor servicioProveedor) {
		this.servicioProveedor = servicioProveedor;
	}

	/**
	 * @return the montoComision
	 */
	public BigDecimal getMontoComision() {
		return montoComision;
	}

	/**
	 * @param montoComision the montoComision to set
	 */
	public void setMontoComision(BigDecimal montoComision) {
		this.montoComision = montoComision;
	}

}
