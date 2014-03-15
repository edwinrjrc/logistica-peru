/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.bean.negocio.Ubigeo;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author Edwin
 * 
 */
@ManagedBean(name = "proveedorMBean")
@SessionScoped()
public class ProveedorMBean extends BaseMBean{

	private List<Proveedor> listaProveedores;

	private Proveedor proveedor;
	private Direccion direccion;
	private Contacto contacto;

	private boolean nuevoProveedor;
	private boolean editarProveedor;
	private boolean nuevaDireccion;
	private boolean editarDireccion;
	private boolean nuevoContacto;
	private boolean editarContacto;
	
	private String nombreFormulario;
	private String nombreFormularioDireccion;
	private String nombreFormularioContacto;
	
	private List<SelectItem> listaProvincia;
	private List<SelectItem> listaDistrito;
	
	private SoporteServicio soporteServicio;
	private NegocioServicio negocioServicio;

	/**
	 * 
	 */
	public ProveedorMBean() {
		try {
			soporteServicio = new SoporteServicioImpl();
			
			ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public void nuevoProveedor(){
		this.setNuevoProveedor(true);
		this.setEditarProveedor(false);
		this.setProveedor(null);
		this.setNombreFormulario("Nuevo Proveedor");
	}
	
	public void nuevaDireccion(){
		this.setNombreFormularioDireccion("Nueva Dirección");
		this.setNuevaDireccion(true);
		this.setEditarDireccion(false);
		this.setDireccion(null);
	}
	
	public void nuevoContacto(){
		this.setNombreFormularioContacto("Nuevo Contacto");
		this.setNuevoContacto(true);
		this.setEditarContacto(false);
	}
	
	public void agregarContacto(){
		try {
			this.getProveedor().getListaContactos().add(negocioServicio.agregarContacto(getContacto()));
			
			this.setContacto(null);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void buscarProveedor(){
		System.out.println("buscar proveedor");
	}
	
	public void ejecutarMetodo(){
		if (this.isNuevoProveedor()){
			try {
				negocioServicio.registrarProveedor(getProveedor());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void consultarProveedor(int codigoProveedor){
		
	}
	
	public void agregarDireccion(){
		try {
			this.getProveedor().getListaDirecciones().add(negocioServicio.agregarDireccion(getDireccion()));
			
			this.setDireccion(null);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void agregarTelefonoDireccion(){
		Telefono telefono = new Telefono();
		telefono.setId(this.getDireccion().getTelefonos().size()+1);
		this.getDireccion().getTelefonos().add(telefono);
	}
	
	public void agregarTelefonoContacto(){
		Telefono telefono = new Telefono();
		telefono.setId(this.getContacto().getListaTelefonos().size()+1);
		this.getContacto().getListaTelefonos().add(telefono);
	}
	
	public void eliminarTelefono(Telefono telefono){
		this.getDireccion().getTelefonos().remove(telefono);
	}
	
	public void eliminarTelefonoContacto(Telefono telefono){
		this.getContacto().getListaTelefonos().remove(telefono);
	}
	
	public void buscarProvincias(ValueChangeEvent e){
		this.setListaProvincia(null);
		this.setListaDistrito(null);
		this.getDireccion().getUbigeo().setProvincia(null);
		this.getDireccion().getUbigeo().setDistrito(null);
	}
	
	public void buscarDistrito(ValueChangeEvent e){
		this.setListaDistrito(null);
		this.getDireccion().getUbigeo().setDistrito(null);
	}
	/**
	 * @return the listaProveedores
	 */
	public List<Proveedor> getListaProveedores() {
		return listaProveedores;
	}

	/**
	 * @param listaProveedores the listaProveedores to set
	 */
	public void setListaProveedores(List<Proveedor> listaProveedores) {
		this.listaProveedores = listaProveedores;
	}

	/**
	 * @return the proveedor
	 */
	public Proveedor getProveedor() {
		if (proveedor == null){
			proveedor = new Proveedor();
		}
		return proveedor;
	}

	/**
	 * @param proveedor the proveedor to set
	 */
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	/**
	 * @return the nuevoProveedor
	 */
	public boolean isNuevoProveedor() {
		return nuevoProveedor;
	}

	/**
	 * @param nuevoProveedor the nuevoProveedor to set
	 */
	public void setNuevoProveedor(boolean nuevoProveedor) {
		this.nuevoProveedor = nuevoProveedor;
	}

	/**
	 * @return the editarProveedor
	 */
	public boolean isEditarProveedor() {
		return editarProveedor;
	}

	/**
	 * @param editarProveedor the editarProveedor to set
	 */
	public void setEditarProveedor(boolean editarProveedor) {
		this.editarProveedor = editarProveedor;
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
	 * @return the nombreFormularioDireccion
	 */
	public String getNombreFormularioDireccion() {
		return nombreFormularioDireccion;
	}

	/**
	 * @param nombreFormularioDireccion the nombreFormularioDireccion to set
	 */
	public void setNombreFormularioDireccion(String nombreFormularioDireccion) {
		this.nombreFormularioDireccion = nombreFormularioDireccion;
	}

	/**
	 * @return the editarDireccion
	 */
	public boolean isEditarDireccion() {
		return editarDireccion;
	}

	/**
	 * @param editarDireccion the editarDireccion to set
	 */
	public void setEditarDireccion(boolean editarDireccion) {
		this.editarDireccion = editarDireccion;
	}

	/**
	 * @return the nuevaDireccion
	 */
	public boolean isNuevaDireccion() {
		return nuevaDireccion;
	}

	/**
	 * @param nuevaDireccion the nuevaDireccion to set
	 */
	public void setNuevaDireccion(boolean nuevaDireccion) {
		this.nuevaDireccion = nuevaDireccion;
	}

	/**
	 * @return the direccion
	 */
	public Direccion getDireccion() {
		if (direccion == null){
			direccion = new Direccion();
		}
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the listaProvincia
	 */
	public List<SelectItem> getListaProvincia() {
		try {
			if (listaProvincia == null || listaProvincia.isEmpty()){
				List<BaseVO> lista = soporteServicio.listarCatalogoProvincia(this.getDireccion().getUbigeo().getDepartamento().getCodigoCadena());
				listaProvincia = UtilWeb.convertirSelectItem(lista);
			}
		} catch (SQLException e) {
			listaProvincia = new ArrayList<SelectItem>();
			e.printStackTrace();
		} catch (Exception e){
			listaProvincia = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return listaProvincia;
	}

	/**
	 * @param listaProvincia the listaProvincia to set
	 */
	public void setListaProvincia(List<SelectItem> listaProvincia) {
		this.listaProvincia = listaProvincia;
	}

	/**
	 * @return the listaDistrito
	 */
	public List<SelectItem> getListaDistrito() {
		try {
			if (listaDistrito==null || listaDistrito.isEmpty()){
				Ubigeo ubigeoLocal = this.getDireccion().getUbigeo();
				List<BaseVO> lista = soporteServicio.listarCatalogoDistrito(ubigeoLocal.getDepartamento().getCodigoCadena(), ubigeoLocal.getProvincia().getCodigoCadena());
				listaDistrito = UtilWeb.convertirSelectItem(lista);
			}
			
		} catch (SQLException e) {
			listaDistrito = new ArrayList<SelectItem>();
			e.printStackTrace();
		} catch (Exception e){
			listaDistrito = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return listaDistrito;
	}

	/**
	 * @param listaDistrito the listaDistrito to set
	 */
	public void setListaDistrito(List<SelectItem> listaDistrito) {
		this.listaDistrito = listaDistrito;
	}

	/**
	 * @return the nombreFormularioContacto
	 */
	public String getNombreFormularioContacto() {
		return nombreFormularioContacto;
	}

	/**
	 * @param nombreFormularioContacto the nombreFormularioContacto to set
	 */
	public void setNombreFormularioContacto(String nombreFormularioContacto) {
		this.nombreFormularioContacto = nombreFormularioContacto;
	}

	/**
	 * @return the contacto
	 */
	public Contacto getContacto() {
		if (contacto == null){
			contacto = new Contacto();
		}
		return contacto;
	}

	/**
	 * @param contacto the contacto to set
	 */
	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}

	/**
	 * @return the nuevoContacto
	 */
	public boolean isNuevoContacto() {
		return nuevoContacto;
	}

	/**
	 * @param nuevoContacto the nuevoContacto to set
	 */
	public void setNuevoContacto(boolean nuevoContacto) {
		this.nuevoContacto = nuevoContacto;
	}

	/**
	 * @return the editarContacto
	 */
	public boolean isEditarContacto() {
		return editarContacto;
	}

	/**
	 * @param editarContacto the editarContacto to set
	 */
	public void setEditarContacto(boolean editarContacto) {
		this.editarContacto = editarContacto;
	}

}
