/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author edwreb
 *
 */
public class ServicioAgencia extends BaseNegocio{
	/**
	 * 
	 */
	private static final long serialVersionUID = 52998145587384961L;
	
	private Cliente cliente;
	private Date fechaServicio;
	private int cantidadServicios;
	private BaseVO destino;
	private BaseVO formaPago;
	private BaseVO estadoPago;
	private int nroCuotas;
	private BigDecimal tea;
	private BigDecimal valorCuota;
	private Date fechaPrimerCuota;
	private Date fechaUltimaCuota;
	
	private List<DetalleServicioAgencia> listaDetalleServicio;
	

	/**
	 * 
	 */
	public ServicioAgencia() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}



	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
	 * @return the cantidadServicios
	 */
	public int getCantidadServicios() {
		return cantidadServicios;
	}



	/**
	 * @param cantidadServicios the cantidadServicios to set
	 */
	public void setCantidadServicios(int cantidadServicios) {
		this.cantidadServicios = cantidadServicios;
	}



	/**
	 * @return the destino
	 */
	public BaseVO getDestino() {
		return destino;
	}



	/**
	 * @param destino the destino to set
	 */
	public void setDestino(BaseVO destino) {
		this.destino = destino;
	}



	/**
	 * @return the listaDetalleServicio
	 */
	public List<DetalleServicioAgencia> getListaDetalleServicio() {
		return listaDetalleServicio;
	}



	/**
	 * @param listaDetalleServicio the listaDetalleServicio to set
	 */
	public void setListaDetalleServicio(List<DetalleServicioAgencia> listaDetalleServicio) {
		this.listaDetalleServicio = listaDetalleServicio;
	}

	public BigDecimal getMontoTotal(){
		BigDecimal monto = BigDecimal.ZERO;
		
		if (this.listaDetalleServicio != null && !this.listaDetalleServicio.isEmpty()){
			for (DetalleServicioAgencia detalleServicio : this.listaDetalleServicio) {
				
				monto = monto.add(detalleServicio.getTotalServicio());
			}
		}
		
		return monto;
	}



	/**
	 * @return the formaPago
	 */
	public BaseVO getFormaPago() {
		return formaPago;
	}



	/**
	 * @param formaPago the formaPago to set
	 */
	public void setFormaPago(BaseVO formaPago) {
		this.formaPago = formaPago;
	}



	/**
	 * @return the estadoPago
	 */
	public BaseVO getEstadoPago() {
		return estadoPago;
	}



	/**
	 * @param estadoPago the estadoPago to set
	 */
	public void setEstadoPago(BaseVO estadoPago) {
		this.estadoPago = estadoPago;
	}



	/**
	 * @return the nroCuotas
	 */
	public int getNroCuotas() {
		return nroCuotas;
	}



	/**
	 * @param nroCuotas the nroCuotas to set
	 */
	public void setNroCuotas(int nroCuotas) {
		this.nroCuotas = nroCuotas;
	}



	/**
	 * @return the tea
	 */
	public BigDecimal getTea() {
		return tea;
	}



	/**
	 * @param tea the tea to set
	 */
	public void setTea(BigDecimal tea) {
		this.tea = tea;
	}



	/**
	 * @return the valorCuota
	 */
	public BigDecimal getValorCuota() {
		return valorCuota;
	}



	/**
	 * @param valorCuota the valorCuota to set
	 */
	public void setValorCuota(BigDecimal valorCuota) {
		this.valorCuota = valorCuota;
	}



	/**
	 * @return the fechaPrimerCuota
	 */
	public Date getFechaPrimerCuota() {
		return fechaPrimerCuota;
	}



	/**
	 * @param fechaPrimerCuota the fechaPrimerCuota to set
	 */
	public void setFechaPrimerCuota(Date fechaPrimerCuota) {
		this.fechaPrimerCuota = fechaPrimerCuota;
	}



	/**
	 * @return the fechaUltimaCuota
	 */
	public Date getFechaUltimaCuota() {
		return fechaUltimaCuota;
	}



	/**
	 * @param fechaUltimaCuota the fechaUltimaCuota to set
	 */
	public void setFechaUltimaCuota(Date fechaUltimaCuota) {
		this.fechaUltimaCuota = fechaUltimaCuota;
	}
}
