package pe.com.logistica.bean.negocio;

import java.util.List;

/**
 * @author Edwin
 * @version 1.0
 * @created 14-dic-2013 01:14:34 p.m.
 */
public class Contacto extends Persona {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4988270415170041781L;
	private List<String> listaCorreos;
	private List<String> listaTelefonos;

	public Contacto() {

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * @return the listaCorreos
	 */
	public List<String> getListaCorreos() {
		return listaCorreos;
	}

	/**
	 * @param listaCorreos
	 *            the listaCorreos to set
	 */
	public void setListaCorreos(List<String> listaCorreos) {
		this.listaCorreos = listaCorreos;
	}

	/**
	 * @return the listaTelefonos
	 */
	public List<String> getListaTelefonos() {
		return listaTelefonos;
	}

	/**
	 * @param listaTelefonos
	 *            the listaTelefonos to set
	 */
	public void setListaTelefonos(List<String> listaTelefonos) {
		this.listaTelefonos = listaTelefonos;
	}

}