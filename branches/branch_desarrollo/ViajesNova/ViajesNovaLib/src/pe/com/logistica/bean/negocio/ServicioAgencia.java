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
 * @author edwreb
 *
 */
public class ServicioAgencia extends BaseNegocio{
	/**
	 * 
	 */
	private static final long serialVersionUID = 52998145587384961L;
	
	private Cliente cliente;
	private Cliente cliente2;
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
	
	private BigDecimal montoTotalServicios;
	private BigDecimal montoTotalFee;
	private BigDecimal montoTotalComision;
	
	private List<DetalleServicioAgencia> listaDetalleServicio;
	private List<CronogramaPago> cronogramaPago;
	
	private BaseVO vendedor;

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
		if (cliente == null){
			cliente = new Cliente();
		}
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
	 * @return the listaDetalleServicio
	 */
	public List<DetalleServicioAgencia> getListaDetalleServicio() {
		if (listaDetalleServicio == null){
			listaDetalleServicio = new ArrayList<DetalleServicioAgencia>();
		}
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
		if (formaPago == null){
			formaPago = new BaseVO();
		}
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
		if (estadoPago == null){
			estadoPago = new BaseVO();
		}
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



	/**
	 * @return the montoTotalServicios
	 */
	public BigDecimal getMontoTotalServicios() {
		if (montoTotalServicios == null){
			montoTotalServicios = BigDecimal.ZERO;
		}
		return montoTotalServicios;
	}



	/**
	 * @param montoTotalServicios the montoTotalServicios to set
	 */
	public void setMontoTotalServicios(BigDecimal montoTotalServicios) {
		this.montoTotalServicios = montoTotalServicios;
	}



	/**
	 * @return the cronogramaPago
	 */
	public List<CronogramaPago> getCronogramaPago() {
		if (cronogramaPago == null){
			cronogramaPago = new ArrayList<CronogramaPago>();
		}
		return cronogramaPago;
	}



	/**
	 * @param cronogramaPago the cronogramaPago to set
	 */
	public void setCronogramaPago(List<CronogramaPago> cronogramaPago) {
		this.cronogramaPago = cronogramaPago;
	}



	/**
	 * @return the cliente2
	 */
	public Cliente getCliente2() {
		if (cliente2 == null){
			cliente2 = new Cliente();
		}
		return cliente2;
	}



	/**
	 * @param cliente2 the cliente2 to set
	 */
	public void setCliente2(Cliente cliente2) {
		this.cliente2 = cliente2;
	}



	/**
	 * @return the montoTotalFee
	 */
	public BigDecimal getMontoTotalFee() {
		return montoTotalFee;
	}



	/**
	 * @param montoTotalFee the montoTotalFee to set
	 */
	public void setMontoTotalFee(BigDecimal montoTotalFee) {
		this.montoTotalFee = montoTotalFee;
	}



	/**
	 * @return the montoTotalComision
	 */
	public BigDecimal getMontoTotalComision() {
		return montoTotalComision;
	}



	/**
	 * @param montoTotalComision the montoTotalComision to set
	 */
	public void setMontoTotalComision(BigDecimal montoTotalComision) {
		this.montoTotalComision = montoTotalComision;
	}



	/**
	 * @return the vendedor
	 */
	public BaseVO getVendedor() {
		if (vendedor == null){
			vendedor = new BaseVO();
		}
		return vendedor;
	}



	/**
	 * @param vendedor the vendedor to set
	 */
	public void setVendedor(BaseVO vendedor) {
		this.vendedor = vendedor;
	}
}
