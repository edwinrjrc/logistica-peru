/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.math.BigDecimal;
import java.util.Date;

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
	private BaseVO moneda;
	private BigDecimal cuotaInicial;
	private int nroDias;
	private int nroNoches;
	private Date fechaShower;
	private String observaciones;

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

}
