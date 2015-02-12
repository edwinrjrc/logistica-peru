/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Pais;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "paisMBean")
@SessionScoped()
public class PaisMBean extends BaseMBean {

	private BaseVO continente;
	private Pais pais;
	
	private List<BaseVO> listaContinente;
	private List<BaseVO> listaPaisContinente;
	
	private boolean nuevoPais;
	private boolean editarPais;
	
	private SoporteServicio soporteServicio;
	/**
	 * 
	 */
	public PaisMBean() {
		try {
			ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
			soporteServicio = new SoporteServicioImpl(servletContext);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public String consultarPaises(int idcontinente){
		
		try {
			Maestro hijo = new Maestro();
			hijo.setCodigoMaestro(10);
			hijo.setCodigoEntero(idcontinente);
			this.setContinente(this.soporteServicio.consultarHijoMaestro(hijo));
			this.setListaPaisContinente(this.soporteServicio.consultarPaises(idcontinente));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "irPaises";
	}
	
	public void nuevoPais(){
		this.setNuevoPais(true);
		this.setEditarPais(false);
		this.setPais(null);
		this.setNombreFormulario("Nuevo Pais");
	}
	
	public void ejecutarMetodo(){
		try {
			if (this.isNuevoPais()){
				this.getPais().setContinente(getContinente());
				this.soporteServicio.ingresarPais(getPais());
				this.setShowModal(true);
				this.setTipoModal("1");
				this.setMensajeModal("Pais registrado Satisfactoriamente");
			}
			else if (this.isEditarPais()){
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the listaContinente
	 */
	public List<BaseVO> getListaContinente() {
		try {
			listaContinente = this.soporteServicio.listarContinentes();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaContinente;
	}
	/**
	 * @param listaContinente the listaContinente to set
	 */
	public void setListaContinente(List<BaseVO> listaContinente) {
		this.listaContinente = listaContinente;
	}

	/**
	 * @return the listaPaisContinente
	 */
	public List<BaseVO> getListaPaisContinente() {
		
		this.consultarPaises(getContinente().getCodigoEntero());
		this.setShowModal(false);
		
		return listaPaisContinente;
	}

	/**
	 * @param listaPaisContinente the listaPaisContinente to set
	 */
	public void setListaPaisContinente(List<BaseVO> listaPaisContinente) {
		this.listaPaisContinente = listaPaisContinente;
	}

	/**
	 * @return the continente
	 */
	public BaseVO getContinente() {
		return continente;
	}

	/**
	 * @param continente the continente to set
	 */
	public void setContinente(BaseVO continente) {
		this.continente = continente;
	}

	/**
	 * @return the nuevoPais
	 */
	public boolean isNuevoPais() {
		return nuevoPais;
	}

	/**
	 * @param nuevoPais the nuevoPais to set
	 */
	public void setNuevoPais(boolean nuevoPais) {
		this.nuevoPais = nuevoPais;
	}

	/**
	 * @return the editarPais
	 */
	public boolean isEditarPais() {
		return editarPais;
	}

	/**
	 * @param editarPais the editarPais to set
	 */
	public void setEditarPais(boolean editarPais) {
		this.editarPais = editarPais;
	}

	/**
	 * @return the pais
	 */
	public Pais getPais() {
		if (pais == null){
			pais = new Pais();
		}
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(Pais pais) {
		this.pais = pais;
	}

}
