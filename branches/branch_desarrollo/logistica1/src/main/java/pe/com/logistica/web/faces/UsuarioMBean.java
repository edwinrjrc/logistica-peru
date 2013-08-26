/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.naming.NamingException;

import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.web.servicio.SeguridadServicio;
import pe.com.logistica.web.servicio.impl.SeguridadServicioImpl;


/**
 * @author Edwin
 *
 */
@ManagedBean(name="usuarioMBean")
@SessionScoped()
public class UsuarioMBean {

	
	private List<Usuario> listaUsuarios;
	
	private Usuario usuario;
	
	private String reCredencial;
	private String nombreFormulario;
	private String msjeError;
	
	private String mensajeModal;
	private String tipoModal;
	private String modalNombre;
	
	private boolean nuevoUsuario;
	private boolean editarUsuario;
	
	private boolean showError;
	private boolean showModal;
	
	
	private SeguridadServicio seguridadServicio;
	/**
	 * 
	 */
	public UsuarioMBean() {
		try {
			seguridadServicio = new SeguridadServicioImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		usuario = new Usuario();
	}
	
	public void nuevoUsuario(){
		this.setNuevoUsuario(true);
		this.setEditarUsuario(false);
		this.setUsuario(null);
		this.setNombreFormulario("Nuevo Usuario");
	}
	
	public void consultarUsuario(Integer id){
		try {
			this.setNombreFormulario("Editar Usuario");
			this.setEditarUsuario(true);
			this.setNuevoUsuario(false);
			this.setUsuario(this.seguridadServicio.consultarUsuario(id));
			System.out.println("clave ::"+this.getUsuario().getCredencial());
			this.setReCredencial(this.getUsuario().getCredencial());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void registrarUsuario(){
		try {
			seguridadServicio.registrarUsuario(getUsuario());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ejecutarMetodo(ActionEvent e){
		try {
			if (this.isNuevoUsuario()){
				seguridadServicio.registrarUsuario(getUsuario());
			}
			else if(this.isEditarUsuario()){
				seguridadServicio.actualizarUsuario(usuario);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * @return the listaUsuarios
	 */
	public List<Usuario> getListaUsuarios() {
		try {
			listaUsuarios = seguridadServicio.listarUsuarios();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaUsuarios;
	}
	/**
	 * @param listaUsuarios the listaUsuarios to set
	 */
	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		if (usuario == null){
			usuario = new Usuario();
		}
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return the reCredencial
	 */
	public String getReCredencial() {
		return reCredencial;
	}
	/**
	 * @param reCredencial the reCredencial to set
	 */
	public void setReCredencial(String reCredencial) {
		this.reCredencial = reCredencial;
	}

	/**
	 * @return the nuevoUsuario
	 */
	public boolean isNuevoUsuario() {
		return nuevoUsuario;
	}

	/**
	 * @param nuevoUsuario the nuevoUsuario to set
	 */
	public void setNuevoUsuario(boolean nuevoUsuario) {
		this.nuevoUsuario = nuevoUsuario;
	}

	/**
	 * @return the editarUsuario
	 */
	public boolean isEditarUsuario() {
		return editarUsuario;
	}

	/**
	 * @param editarUsuario the editarUsuario to set
	 */
	public void setEditarUsuario(boolean editarUsuario) {
		this.editarUsuario = editarUsuario;
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
	 * @return the showError
	 */
	public boolean isShowError() {
		return showError;
	}

	/**
	 * @param showError the showError to set
	 */
	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	/**
	 * @return the msjeError
	 */
	public String getMsjeError() {
		return msjeError;
	}

	/**
	 * @param msjeError the msjeError to set
	 */
	public void setMsjeError(String msjeError) {
		this.msjeError = msjeError;
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
	 * @return the seguridadServicio
	 */
	public SeguridadServicio getSeguridadServicio() {
		return seguridadServicio;
	}

	/**
	 * @param seguridadServicio the seguridadServicio to set
	 */
	public void setSeguridadServicio(SeguridadServicio seguridadServicio) {
		this.seguridadServicio = seguridadServicio;
	}

}
