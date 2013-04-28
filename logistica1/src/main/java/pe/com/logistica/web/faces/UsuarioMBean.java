/**
 * 
 */
package pe.com.logistica.web.faces;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

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
	
	private boolean nuevoUsuario;
	private boolean editarUsuario;
	
	private SeguridadServicio seguridadServicio;
	/**
	 * 
	 */
	public UsuarioMBean() {
		seguridadServicio = new SeguridadServicioImpl();
		usuario = new Usuario();
	}
	
	public void nuevoUsuario(){
		this.setNuevoUsuario(true);
		this.setEditarUsuario(false);
		this.setUsuario(null);
		this.setNombreFormulario("Nuevo Usuario");
	}
	
	public void consultarUsuario(Integer id){
		this.setNombreFormulario("Editar Usuario");
		this.setEditarUsuario(true);
		this.setNuevoUsuario(false);
		this.setUsuario(this.seguridadServicio.consultarUsuario(id));
		System.out.println("clave ::"+this.getUsuario().getCredencial());
		this.setReCredencial(this.getUsuario().getCredencial());
	}
	
	public void registrarUsuario(){
		seguridadServicio.registrarUsuario(getUsuario());
	}
	
	public void ejecutarMetodo(ActionEvent e){
		System.out.println("ejecutar metodo:.");
		System.out.println("nuevo ::"+this.isNuevoUsuario());
		System.out.println("editar ::"+this.isEditarUsuario());
		if (this.isNuevoUsuario()){
			seguridadServicio.registrarUsuario(getUsuario());
		}
		else if(this.isEditarUsuario()){
			seguridadServicio.actualizarUsuario(usuario);
		}
	}
	/**
	 * @return the listaUsuarios
	 */
	public List<Usuario> getListaUsuarios() {
		listaUsuarios = seguridadServicio.listarUsuarios();
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

}
