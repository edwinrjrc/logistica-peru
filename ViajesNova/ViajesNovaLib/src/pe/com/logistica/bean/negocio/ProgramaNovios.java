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
public class ProgramaNovios extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5022606214850118452L;

	private Cliente novia;
	private Cliente novio;
	private Destino destino;
	private String codigoNovios;
	private Date fechaBoda;
	private Date fechaViaje;
	private BaseVO moneda;
	private BigDecimal cuotaInicial;
	private int nroDias;
	private int nroNoches;
	private Date fechaShower;
	private String observaciones;
	
	private List<Cliente> listaInvitados;
	private List<ServicioNovios> listaServicios;
	private int cantidadInvitados;
	
	private BigDecimal montoTotalServiciosPrograma;
	private BigDecimal montoIgvServiciosPrograma;
	private BigDecimal porcentajeIgv;
	private BigDecimal montoSinIgvServiciosPrograma;

	/**
	 * 
	 */
	public ProgramaNovios() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the novia
	 */
	public Cliente getNovia() {
		if (novia == null){
			novia = new Cliente();
		}
		return novia;
	}

	/**
	 * @param novia
	 *            the novia to set
	 */
	public void setNovia(Cliente novia) {
		this.novia = novia;
	}

	/**
	 * @return the novio
	 */
	public Cliente getNovio() {
		if (novio == null){
			novio = new Cliente();
		}
		return novio;
	}

	/**
	 * @param novio
	 *            the novio to set
	 */
	public void setNovio(Cliente novio) {
		this.novio = novio;
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
	 * @param destino
	 *            the destino to set
	 */
	public void setDestino(Destino destino) {
		this.destino = destino;
	}

	/**
	 * @return the codigoNovios
	 */
	public String getCodigoNovios() {
		return codigoNovios;
	}

	/**
	 * @param codigoNovios
	 *            the codigoNovios to set
	 */
	public void setCodigoNovios(String codigoNovios) {
		this.codigoNovios = codigoNovios;
	}

	/**
	 * @return the fechaBoda
	 */
	public Date getFechaBoda() {
		return fechaBoda;
	}

	/**
	 * @param fechaBoda
	 *            the fechaBoda to set
	 */
	public void setFechaBoda(Date fechaBoda) {
		this.fechaBoda = fechaBoda;
	}

	/**
	 * @return the moneda
	 */
	public BaseVO getMoneda() {
		if (moneda == null){
			moneda = new BaseVO();
		}
		return moneda;
	}

	/**
	 * @param moneda
	 *            the moneda to set
	 */
	public void setMoneda(BaseVO moneda) {
		this.moneda = moneda;
	}

	/**
	 * @return the cuotaInicial
	 */
	public BigDecimal getCuotaInicial() {
		return cuotaInicial;
	}

	/**
	 * @param cuotaInicial
	 *            the cuotaInicial to set
	 */
	public void setCuotaInicial(BigDecimal cuotaInicial) {
		this.cuotaInicial = cuotaInicial;
	}

	/**
	 * @return the nroDias
	 */
	public int getNroDias() {
		return nroDias;
	}

	/**
	 * @param nroDias
	 *            the nroDias to set
	 */
	public void setNroDias(int nroDias) {
		this.nroDias = nroDias;
	}

	/**
	 * @return the nroNoches
	 */
	public int getNroNoches() {
		return nroNoches;
	}

	/**
	 * @param nroNoches
	 *            the nroNoches to set
	 */
	public void setNroNoches(int nroNoches) {
		this.nroNoches = nroNoches;
	}

	/**
	 * @return the fechaShower
	 */
	public Date getFechaShower() {
		return fechaShower;
	}

	/**
	 * @param fechaShower
	 *            the fechaShower to set
	 */
	public void setFechaShower(Date fechaShower) {
		this.fechaShower = fechaShower;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones
	 *            the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the fechaViaje
	 */
	public Date getFechaViaje() {
		return fechaViaje;
	}

	/**
	 * @param fechaViaje the fechaViaje to set
	 */
	public void setFechaViaje(Date fechaViaje) {
		this.fechaViaje = fechaViaje;
	}

	/**
	 * @return the listaInvitados
	 */
	public List<Cliente> getListaInvitados() {
		if (listaInvitados == null){
			listaInvitados = new ArrayList<Cliente>();
		}
		return listaInvitados;
	}

	/**
	 * @param listaInvitados the listaInvitados to set
	 */
	public void setListaInvitados(List<Cliente> listaInvitados) {
		this.listaInvitados = listaInvitados;
	}

	/**
	 * @return the cantidadInvitados
	 */
	public int getCantidadInvitados() {
		return cantidadInvitados;
	}

	/**
	 * @param cantidadInvitados the cantidadInvitados to set
	 */
	public void setCantidadInvitados(int cantidadInvitados) {
		this.cantidadInvitados = cantidadInvitados;
	}

	/**
	 * @return the listaServicios
	 */
	public List<ServicioNovios> getListaServicios() {
		return listaServicios;
	}

	/**
	 * @param listaServicios the listaServicios to set
	 */
	public void setListaServicios(List<ServicioNovios> listaServicios) {
		this.listaServicios = listaServicios;
	}

	/**
	 * @return the montoTotalServiciosPrograma
	 */
	public BigDecimal getMontoTotalServiciosPrograma() {
		return montoTotalServiciosPrograma;
	}

	/**
	 * @param montoTotalServiciosPrograma the montoTotalServiciosPrograma to set
	 */
	public void setMontoTotalServiciosPrograma(
			BigDecimal montoTotalServiciosPrograma) {
		this.montoTotalServiciosPrograma = montoTotalServiciosPrograma;
	}

	/**
	 * @return the montoIgvServiciosPrograma
	 */
	public BigDecimal getMontoIgvServiciosPrograma() {
		return montoIgvServiciosPrograma;
	}

	/**
	 * @param montoIgvServiciosPrograma the montoIgvServiciosPrograma to set
	 */
	public void setMontoIgvServiciosPrograma(BigDecimal montoIgvServiciosPrograma) {
		this.montoIgvServiciosPrograma = montoIgvServiciosPrograma;
	}

	/**
	 * @return the montoSinIgvServiciosPrograma
	 */
	public BigDecimal getMontoSinIgvServiciosPrograma() {
		return montoSinIgvServiciosPrograma;
	}

	/**
	 * @param montoSinIgvServiciosPrograma the montoSinIgvServiciosPrograma to set
	 */
	public void setMontoSinIgvServiciosPrograma(
			BigDecimal montoSinIgvServiciosPrograma) {
		this.montoSinIgvServiciosPrograma = montoSinIgvServiciosPrograma;
	}

	/**
	 * @return the porcentajeIgv
	 */
	public BigDecimal getPorcentajeIgv() {
		return porcentajeIgv;
	}

	/**
	 * @param porcentajeIgv the porcentajeIgv to set
	 */
	public void setPorcentajeIgv(BigDecimal porcentajeIgv) {
		this.porcentajeIgv = porcentajeIgv;
	}

}