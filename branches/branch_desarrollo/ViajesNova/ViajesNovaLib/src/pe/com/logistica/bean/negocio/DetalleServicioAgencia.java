/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	private MaestroServicio tipoServicio;
	private String descripcionServicio;
	private Destino origen;
	private Destino destino;
	private BaseVO aerolinea;
	private int dias;
	private int noches;
	private Date fechaServicio;
	private Date fechaIda;
	private Date fechaRegreso;
	private int cantidad;
	private BigDecimal precioUnitario;
	private BigDecimal montoComision;
	private BigDecimal montoIGV;
	private ServicioProveedor servicioProveedor;
	private BaseVO servicioPadre;
	private Consolidador consolidador;
	
	private List<DetalleServicioAgencia> serviciosHijos;
	
	private ConfiguracionTipoServicio configuracionTipoServicio;
	/**
	 * 
	 */
	public DetalleServicioAgencia() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the tipoServicio
	 */
	public MaestroServicio getTipoServicio() {
		if (tipoServicio == null){
			tipoServicio = new MaestroServicio();
		}
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(MaestroServicio tipoServicio) {
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
	public Destino getDestino() {
		if (destino == null){
			destino = new Destino();
		}
		return destino;
	}

	/**
	 * @param destino the destino to set
	 */
	public void setDestino(Destino destino) {
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
		BigDecimal total = BigDecimal.ZERO;
		try {
			BigDecimal cantidadDecimal = BigDecimal.ZERO;
			if (cantidad != 0){
				cantidadDecimal = cantidadDecimal.add(UtilParse.parseIntABigDecimal(cantidad));
			}
			else{
				cantidadDecimal = BigDecimal.ONE;
			}
			total = total.add(this.precioUnitario.multiply(cantidadDecimal));
		} catch (Exception e) {
			total = BigDecimal.ZERO;
		}
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
		if (montoComision == null){
			montoComision = BigDecimal.ZERO;
		}
		return montoComision;
	}

	/**
	 * @param montoComision the montoComision to set
	 */
	public void setMontoComision(BigDecimal montoComision) {
		this.montoComision = montoComision;
	}

	/**
	 * @return the montoIGV
	 */
	public BigDecimal getMontoIGV() {
		if (montoIGV == null){
			montoIGV = BigDecimal.ZERO;
		}
		return montoIGV;
	}

	/**
	 * @param montoIGV the montoIGV to set
	 */
	public void setMontoIGV(BigDecimal montoIGV) {
		this.montoIGV = montoIGV;
	}

	/**
	 * @return the servicioPadre
	 */
	public BaseVO getServicioPadre() {
		if (servicioPadre == null){
			servicioPadre = new BaseVO();
		}
		return servicioPadre;
	}

	/**
	 * @param servicioPadre the servicioPadre to set
	 */
	public void setServicioPadre(BaseVO servicioPadre) {
		this.servicioPadre = servicioPadre;
	}

	/**
	 * @return the serviciosHijos
	 */
	public List<DetalleServicioAgencia> getServiciosHijos() {
		if (serviciosHijos == null){
			serviciosHijos = new ArrayList<DetalleServicioAgencia>();
		}
		return serviciosHijos;
	}

	/**
	 * @param serviciosHijos the serviciosHijos to set
	 */
	public void setServiciosHijos(List<DetalleServicioAgencia> serviciosHijos) {
		this.serviciosHijos = serviciosHijos;
	}

	/**
	 * @return the consolidador
	 */
	public Consolidador getConsolidador() {
		if (consolidador == null){
			consolidador = new Consolidador();
		}
		return consolidador;
	}

	/**
	 * @param consolidador the consolidador to set
	 */
	public void setConsolidador(Consolidador consolidador) {
		this.consolidador = consolidador;
	}

	/**
	 * @return the configuracionTipoServicio
	 */
	public ConfiguracionTipoServicio getConfiguracionTipoServicio() {
		if (configuracionTipoServicio == null) {
			configuracionTipoServicio = new ConfiguracionTipoServicio();
		}
		return configuracionTipoServicio;
	}

	/**
	 * @param configuracionTipoServicio the configuracionTipoServicio to set
	 */
	public void setConfiguracionTipoServicio(ConfiguracionTipoServicio configuracionTipoServicio) {
		this.configuracionTipoServicio = configuracionTipoServicio;
	}

	/**
	 * @return the aerolinea
	 */
	public BaseVO getAerolinea() {
		if (aerolinea == null){
			aerolinea = new BaseVO();
		}
		return aerolinea;
	}

	/**
	 * @param aerolinea the aerolinea to set
	 */
	public void setAerolinea(BaseVO aerolinea) {
		this.aerolinea = aerolinea;
	}

	/**
	 * @return the origen
	 */
	public Destino getOrigen() {
		if (origen == null){
			origen = new Destino();
		}
		return origen;
	}

	/**
	 * @param origen the origen to set
	 */
	public void setOrigen(Destino origen) {
		this.origen = origen;
	}

}
