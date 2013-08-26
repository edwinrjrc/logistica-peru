/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.NamingException;

import pe.com.logistica.bean.negocio.Parametro;
import pe.com.logistica.web.servicio.ParametroServicio;
import pe.com.logistica.web.servicio.impl.ParametroServicioImpl;

/**
 * @author Edwin
 *
 */
@ManagedBean(name="parametroMBean")
@SessionScoped()
public class ParametroMBean {

	private List<Parametro> listaParametros;
	
	private Parametro parametro;
	
	private String nombreFormulario;
	private String mensajeModal;
	private String tipoModal;
	private String modalNombre;
	
	private boolean nuevoParametro;
	private boolean editarParametro;
	private boolean showModal;
	
	private ParametroServicio parametroServicio;
	/**
	 * 
	 */
	public ParametroMBean() {
		try {
			parametroServicio = new ParametroServicioImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public void nuevoParametro(){
		this.setNuevoParametro(true);
		this.setEditarParametro(false);
		this.setParametro(null);
		this.setNombreFormulario("Nuevo Parametro");
	}
	
	public void ejecutarMetodo(){
		try {
			if (this.isNuevoParametro()){
				parametroServicio.registrarParametro(parametro);
				this.setShowModal(true);
				this.setTipoModal("1");
				this.setMensajeModal("Parametro registrado Satisfactoriamente");
			}
			else if(this.isEditarParametro()){
				parametroServicio.actualizarParametro(parametro);
			}
		} catch (Exception e) {
			this.setShowModal(true);
			this.setTipoModal("2");
			this.setMensajeModal(e.getMessage());
		}
	}
	
	public void consultarParametro(Integer id){
		try {
			this.setNombreFormulario("Editar Parametro");
			this.setEditarParametro(true);
			this.setNuevoParametro(false);
			this.setParametro(parametroServicio.consultarParametro(id));
		} catch (SQLException e) {
			e.printStackTrace();
			this.setShowModal(true);
			this.setTipoModal("2");
			this.setMensajeModal(e.getMessage());
		}
	}
	/**x|
	 * @return the listaParametros
	 */
	public List<Parametro> getListaParametros() {
		try {
			parametroServicio = new ParametroServicioImpl();
			listaParametros = parametroServicio.listarParametros();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		return listaParametros;
	}
	/**
	 * @param listaParametros the listaParametros to set
	 */
	public void setListaParametros(List<Parametro> listaParametros) {
		this.listaParametros = listaParametros;
	}
	/**
	 * @return the parametro
	 */
	public Parametro getParametro() {
		if (parametro == null){
			parametro = new Parametro();
		}
		return parametro;
	}
	/**
	 * @param parametro the parametro to set
	 */
	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}
	/**
	 * @return the nombreFormulario
	 */
	public String getNombreFormulario() {
		return nombreFormulario;
	}
	/**
	 * @param nombreFormulario the nombreFormulario to set
	 */
	public void setNombreFormulario(String nombreFormulario) {
		this.nombreFormulario = nombreFormulario;
	}
	/**
	 * @return the nuevoParametro
	 */
	public boolean isNuevoParametro() {
		return nuevoParametro;
	}
	/**
	 * @param nuevoParametro the nuevoParametro to set
	 */
	public void setNuevoParametro(boolean nuevoParametro) {
		this.nuevoParametro = nuevoParametro;
	}
	/**
	 * @return the editarParametro
	 */
	public boolean isEditarParametro() {
		return editarParametro;
	}
	/**
	 * @param editarParametro the editarParametro to set
	 */
	public void setEditarParametro(boolean editarParametro) {
		this.editarParametro = editarParametro;
	}

	/**
	 * @return the mensajeModal
	 */
	public String getMensajeModal() {
		return mensajeModal;
	}

	/**
	 * @param mensajeModal the mensajeModal to set
	 */
	public void setMensajeModal(String mensajeModal) {
		this.mensajeModal = mensajeModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}

	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the tipoModal
	 */
	public String getTipoModal() {
		return tipoModal;
	}

	/**
	 * @param tipoModal the tipoModal to set
	 */
	public void setTipoModal(String tipoModal) {
		this.tipoModal = tipoModal;
	}

	/**
	 * @return the modalNombre
	 */
	public String getModalNombre() {
		return modalNombre;
	}

	/**
	 * @param modalNombre the modalNombre to set
	 */
	public void setModalNombre(String modalNombre) {
		this.modalNombre = modalNombre;
	}

}
