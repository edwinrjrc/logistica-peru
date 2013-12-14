package pe.com.logistica.bean.negocio;

import java.util.List;

import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 * @version 1.0
 * @created 14-dic-2013 01:14:34 p.m.
 */
public class Proveedor extends Persona {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4758923398707358761L;
	private List<Contacto> listaContactos;
	private List<CuentaBancaria> listaCuentas;
	private BaseVO rubro;

	public Proveedor() {

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * @return the listaContactos
	 */
	public List<Contacto> getListaContactos() {
		return listaContactos;
	}

	/**
	 * @param listaContactos
	 *            the listaContactos to set
	 */
	public void setListaContactos(List<Contacto> listaContactos) {
		this.listaContactos = listaContactos;
	}

	/**
	 * @return the listaCuentas
	 */
	public List<CuentaBancaria> getListaCuentas() {
		return listaCuentas;
	}

	/**
	 * @param listaCuentas
	 *            the listaCuentas to set
	 */
	public void setListaCuentas(List<CuentaBancaria> listaCuentas) {
		this.listaCuentas = listaCuentas;
	}

	/**
	 * @return the rubro
	 */
	public BaseVO getRubro() {
		return rubro;
	}

	/**
	 * @param rubro
	 *            the rubro to set
	 */
	public void setRubro(BaseVO rubro) {
		this.rubro = rubro;
	}

}