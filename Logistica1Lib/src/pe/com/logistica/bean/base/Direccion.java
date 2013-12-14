package pe.com.logistica.bean.base;

import java.util.List;

import pe.com.logistica.bean.negocio.Ubigeo;

/**
 * @author Edwin
 * @version 1.0
 * @created 14-dic-2013 01:14:34 p.m.
 */
public class Direccion extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 452591329561802664L;

	private String direccion;
	private String interior;
	private String nombreVia;
	private String nombreZona;
	private String numero;
	private String observaciones;
	private String referencia;
	private List<String> telefonos;
	private BaseVO tipoDireccion;
	private Ubigeo ubigeo;
	private BaseVO via;
	private BaseVO zona;

	public Direccion() {

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the interior
	 */
	public String getInterior() {
		return interior;
	}

	/**
	 * @param interior
	 *            the interior to set
	 */
	public void setInterior(String interior) {
		this.interior = interior;
	}

	/**
	 * @return the nombreVia
	 */
	public String getNombreVia() {
		return nombreVia;
	}

	/**
	 * @param nombreVia
	 *            the nombreVia to set
	 */
	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
	}

	/**
	 * @return the nombreZona
	 */
	public String getNombreZona() {
		return nombreZona;
	}

	/**
	 * @param nombreZona
	 *            the nombreZona to set
	 */
	public void setNombreZona(String nombreZona) {
		this.nombreZona = nombreZona;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
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
	 * @return the referencia
	 */
	public String getReferencia() {
		return referencia;
	}

	/**
	 * @param referencia
	 *            the referencia to set
	 */
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	/**
	 * @return the telefonos
	 */
	public List<String> getTelefonos() {
		return telefonos;
	}

	/**
	 * @param telefonos
	 *            the telefonos to set
	 */
	public void setTelefonos(List<String> telefonos) {
		this.telefonos = telefonos;
	}

	/**
	 * @return the tipoDireccion
	 */
	public BaseVO getTipoDireccion() {
		return tipoDireccion;
	}

	/**
	 * @param tipoDireccion
	 *            the tipoDireccion to set
	 */
	public void setTipoDireccion(BaseVO tipoDireccion) {
		this.tipoDireccion = tipoDireccion;
	}

	/**
	 * @return the ubigeo
	 */
	public Ubigeo getUbigeo() {
		return ubigeo;
	}

	/**
	 * @param ubigeo
	 *            the ubigeo to set
	 */
	public void setUbigeo(Ubigeo ubigeo) {
		this.ubigeo = ubigeo;
	}

	/**
	 * @return the via
	 */
	public BaseVO getVia() {
		return via;
	}

	/**
	 * @param via
	 *            the via to set
	 */
	public void setVia(BaseVO via) {
		this.via = via;
	}

	/**
	 * @return the zona
	 */
	public BaseVO getZona() {
		return zona;
	}

	/**
	 * @param zona
	 *            the zona to set
	 */
	public void setZona(BaseVO zona) {
		this.zona = zona;
	}

}