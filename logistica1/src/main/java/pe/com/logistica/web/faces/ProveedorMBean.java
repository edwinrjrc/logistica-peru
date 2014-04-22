/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.bean.negocio.Ubigeo;
import pe.com.logistica.bean.negocio.Usuario;
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
public class ProveedorMBean extends BaseMBean {

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
	private boolean direccionAgregada;
	private boolean contactoAgregada;

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
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			soporteServicio = new SoporteServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public void nuevoProveedor() {
		this.setNuevoProveedor(true);
		this.setEditarProveedor(false);
		this.setProveedor(null);
		this.setNombreFormulario("Nuevo Proveedor");
	}

	public void nuevaDireccion() {
		this.setNombreFormularioDireccion("Nueva Dirección");
		this.setNuevaDireccion(true);
		this.setEditarDireccion(false);
		this.setListaDistrito(null);
		this.setListaProvincia(null);
		this.setDireccion(null);
		this.setDireccionAgregada(false);
	}

	public void nuevoContacto() {
		this.setContacto(null);
		this.setNombreFormularioContacto("Nuevo Contacto");
		this.setNuevoContacto(true);
		this.setEditarContacto(false);
		this.setContactoAgregada(false);
	}

	public void agregarContacto(ActionEvent e) {
		try {
			if (this.validarContacto(e)) {
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");
				getContacto().setUsuarioCreacion(usuario.getUsuario());
				getContacto().setIpCreacion(obtenerRequest().getRemoteAddr());
				this.getProveedor().getListaContactos()
						.add(negocioServicio.agregarContacto(getContacto()));

				this.setContactoAgregada(true);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void editarContactoProveedor(Contacto contactoLista) {
		this.setNombreFormularioDireccion("Editar Contacto");
		this.setNuevoContacto(false);
		this.setEditarContacto(true);
		this.setContacto(contactoLista);
	}

	public void eliminarContactoProveedor(Contacto contactoLista) {
		this.getProveedor().getListaContactos().remove(contactoLista);
	}

	public void buscarProveedor() {
		System.out.println("buscar proveedor");
	}

	public void ejecutarMetodo(ActionEvent e) {
		try {
			if (this.isNuevoProveedor()) {
				if (validarProveedor(e)) {
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getProveedor().setUsuarioCreacion(usuario.getUsuario());
					getProveedor().setIpCreacion(
							obtenerRequest().getRemoteAddr());
					this.setShowModal(negocioServicio
							.registrarProveedor(getProveedor()));
					this.setTipoModal("1");
					this.setMensajeModal("Proveedor registrado Satisfactoriamente");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			this.setShowModal(true);
			this.setTipoModal("2");
			this.setMensajeModal(ex.getMessage());
		}
	}

	private boolean validarProveedor(ActionEvent e) {
		boolean resultado = true;
		String idFormulario = "idFormProveedor";
		int tipoDocDNI = UtilWeb.obtenerEnteroPropertieMaestro(
				"tipoDocumentoDNI", "aplicacionDatos");
		int tipoDocCE = UtilWeb.obtenerEnteroPropertieMaestro(
				"tipoDocumentoCE", "aplicacionDatos");
		int tipoDocRUC = UtilWeb.obtenerEnteroPropertieMaestro(
				"tipoDocumentoRUC", "aplicacionDatos");

		if (StringUtils.isBlank(getContacto().getApellidoPaterno())) {
			this.agregarMensaje(idFormulario + ":idApePat",
					"Ingrese el apellido paterno", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (tipoDocDNI == getProveedor().getDocumentoIdentidad()
				.getTipoDocumento().getCodigoEntero().intValue()
				|| tipoDocCE == getProveedor().getDocumentoIdentidad()
						.getTipoDocumento().getCodigoEntero().intValue()) {
			if (StringUtils.isBlank(getProveedor().getApellidoMaterno())) {
				this.agregarMensaje(idFormulario + ":idApeMatPro",
						"Ingrese el apellido materno", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (StringUtils.isBlank(getProveedor().getApellidoPaterno())) {
				this.agregarMensaje(idFormulario + ":idApePatPro",
						"Ingrese el apellido paterno", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (StringUtils.isBlank(getProveedor().getApellidoPaterno())) {
				this.agregarMensaje(idFormulario + ":idApePatPro",
						"Ingrese el apellido paterno", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (StringUtils.isBlank(getProveedor().getNombres())) {
				this.agregarMensaje(idFormulario + ":idNomPro",
						"Ingrese los nombres", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
		}
		if (tipoDocRUC == getProveedor().getDocumentoIdentidad()
				.getTipoDocumento().getCodigoEntero().intValue()){
			if (StringUtils.isBlank(getProveedor().getNombres())) {
				this.agregarMensaje(idFormulario + ":idRazSocPro",
						"Ingrese la razon social", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
		}
		return resultado;
	}

	public void consultarProveedor(int codigoProveedor) {

	}

	public void agregarDireccion(ActionEvent e) {
		try {
			this.setDireccionAgregada(false);
			if (this.validarDireccion(e)) {
				this.getProveedor().getListaDirecciones()
						.add(negocioServicio.agregarDireccion(getDireccion()));
				this.setDireccionAgregada(true);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void editarDireccionProveedor(Direccion direccionLista) {
		this.setNombreFormularioDireccion("Editar Dirección");
		this.setNuevaDireccion(false);
		this.setEditarDireccion(true);
		this.setDireccion(direccionLista);
	}

	public void eliminarDireccionProveedor(Direccion direccionLista) {
		this.getProveedor().getListaDirecciones().remove(direccionLista);
	}

	public void agregarTelefonoDireccion() {
		Telefono telefono = new Telefono();
		telefono.setId(this.getDireccion().getTelefonos().size() + 1);
		this.getDireccion().getTelefonos().add(telefono);
	}

	public void agregarTelefonoContacto() {
		Telefono telefono = new Telefono();
		telefono.setId(this.getContacto().getListaTelefonos().size() + 1);
		this.getContacto().getListaTelefonos().add(telefono);
	}

	public void eliminarTelefono(Telefono telefono) {
		this.getDireccion().getTelefonos().remove(telefono);
	}

	public void eliminarTelefonoContacto(Telefono telefono) {
		this.getContacto().getListaTelefonos().remove(telefono);
	}

	public void buscarProvincias(ValueChangeEvent e) {
		this.setListaProvincia(null);
		this.setListaDistrito(null);
		this.getDireccion().getUbigeo().setProvincia(null);
		this.getDireccion().getUbigeo().setDistrito(null);
	}

	public void buscarDistrito(ValueChangeEvent e) {
		this.setListaDistrito(null);
		this.getDireccion().getUbigeo().setDistrito(null);
	}

	private boolean validarContacto(ActionEvent e) {
		boolean resultado = true;
		String idFormulario = "idFCPr";
		if (StringUtils.isBlank(getContacto().getApellidoPaterno())) {
			this.agregarMensaje(idFormulario + ":idApePat",
					"Ingrese el apellido paterno", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.isBlank(getContacto().getApellidoMaterno())) {
			this.agregarMensaje(idFormulario + ":idApeMat",
					"Ingrese el apellido materno", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.isBlank(getContacto().getNombres())) {
			this.agregarMensaje(idFormulario + ":idForProNom",
					"Ingrese los nombres", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (getContacto().getArea().getCodigoEntero() == null) {
			this.agregarMensaje(idFormulario + ":idSelAreCon",
					"Seleccione el area del contacto", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}

		return resultado;
	}

	private boolean validarDireccion(ActionEvent e) {
		boolean resultado = true;
		String idFormulario = "idFDPr";
		if (getDireccion().getVia().getCodigoEntero() == null) {
			this.agregarMensaje(idFormulario + ":idFDSelTipoVia",
					"Seleccione el tipo de via", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		} else if (getDireccion().getVia().getCodigoEntero().intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idFDSelTipoVia",
					"Seleccione el tipo de via", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.isBlank(getDireccion().getNombreVia())) {
			this.agregarMensaje(idFormulario + ":idFDInNomVia",
					"Ingrese el nombre de via", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.isBlank(getDireccion().getUbigeo().getDepartamento()
				.getCodigoCadena())) {
			this.agregarMensaje(idFormulario + ":idDepartamentoDireccion",
					"Seleccione el departamento", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.isBlank(getDireccion().getUbigeo().getProvincia()
				.getCodigoCadena())) {
			this.agregarMensaje(idFormulario + ":idProvinciaDireccion",
					"Seleccione la provincia", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.isBlank(getDireccion().getUbigeo().getDistrito()
				.getCodigoCadena())) {
			this.agregarMensaje(idFormulario + ":idDistritoDireccion",
					"Seleccione el distrito", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}

		return resultado;
	}

	/**
	 * @return the listaProveedores
	 */
	public List<Proveedor> getListaProveedores() {
		try {
			listaProveedores = this.negocioServicio
					.listarProveedor(getProveedor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaProveedores;
	}

	/**
	 * @param listaProveedores
	 *            the listaProveedores to set
	 */
	public void setListaProveedores(List<Proveedor> listaProveedores) {
		this.listaProveedores = listaProveedores;
	}

	/**
	 * @return the proveedor
	 */
	public Proveedor getProveedor() {
		if (proveedor == null) {
			proveedor = new Proveedor();
		}
		return proveedor;
	}

	/**
	 * @param proveedor
	 *            the proveedor to set
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
	 * @param nuevoProveedor
	 *            the nuevoProveedor to set
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
	 * @param editarProveedor
	 *            the editarProveedor to set
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
	 * @param nombreFormulario
	 *            the nombreFormulario to set
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
	 * @param nombreFormularioDireccion
	 *            the nombreFormularioDireccion to set
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
	 * @param editarDireccion
	 *            the editarDireccion to set
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
	 * @param nuevaDireccion
	 *            the nuevaDireccion to set
	 */
	public void setNuevaDireccion(boolean nuevaDireccion) {
		this.nuevaDireccion = nuevaDireccion;
	}

	/**
	 * @return the direccion
	 */
	public Direccion getDireccion() {
		if (direccion == null) {
			direccion = new Direccion();
		}
		return direccion;
	}

	/**
	 * @param direccion
	 *            the direccion to set
	 */
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the listaProvincia
	 */
	public List<SelectItem> getListaProvincia() {
		try {
			if (listaProvincia == null || listaProvincia.isEmpty()) {
				List<BaseVO> lista = soporteServicio
						.listarCatalogoProvincia(this.getDireccion()
								.getUbigeo().getDepartamento()
								.getCodigoCadena());
				listaProvincia = UtilWeb.convertirSelectItem(lista);
			}
		} catch (SQLException e) {
			listaProvincia = new ArrayList<SelectItem>();
			e.printStackTrace();
		} catch (Exception e) {
			listaProvincia = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return listaProvincia;
	}

	/**
	 * @param listaProvincia
	 *            the listaProvincia to set
	 */
	public void setListaProvincia(List<SelectItem> listaProvincia) {
		this.listaProvincia = listaProvincia;
	}

	/**
	 * @return the listaDistrito
	 */
	public List<SelectItem> getListaDistrito() {
		try {
			if (listaDistrito == null || listaDistrito.isEmpty()) {
				Ubigeo ubigeoLocal = this.getDireccion().getUbigeo();
				List<BaseVO> lista = soporteServicio.listarCatalogoDistrito(
						ubigeoLocal.getDepartamento().getCodigoCadena(),
						ubigeoLocal.getProvincia().getCodigoCadena());
				listaDistrito = UtilWeb.convertirSelectItem(lista);
			}

		} catch (SQLException e) {
			listaDistrito = new ArrayList<SelectItem>();
			e.printStackTrace();
		} catch (Exception e) {
			listaDistrito = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return listaDistrito;
	}

	/**
	 * @param listaDistrito
	 *            the listaDistrito to set
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
	 * @param nombreFormularioContacto
	 *            the nombreFormularioContacto to set
	 */
	public void setNombreFormularioContacto(String nombreFormularioContacto) {
		this.nombreFormularioContacto = nombreFormularioContacto;
	}

	/**
	 * @return the contacto
	 */
	public Contacto getContacto() {
		if (contacto == null) {
			contacto = new Contacto();
		}
		return contacto;
	}

	/**
	 * @param contacto
	 *            the contacto to set
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
	 * @param nuevoContacto
	 *            the nuevoContacto to set
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
	 * @param editarContacto
	 *            the editarContacto to set
	 */
	public void setEditarContacto(boolean editarContacto) {
		this.editarContacto = editarContacto;
	}

	/**
	 * @return the direccionAgregada
	 */
	public boolean isDireccionAgregada() {
		return direccionAgregada;
	}

	/**
	 * @param direccionAgregada
	 *            the direccionAgregada to set
	 */
	public void setDireccionAgregada(boolean direccionAgregada) {
		this.direccionAgregada = direccionAgregada;
	}

	/**
	 * @return the contactoAgregada
	 */
	public boolean isContactoAgregada() {
		return contactoAgregada;
	}

	/**
	 * @param contactoAgregada
	 *            the contactoAgregada to set
	 */
	public void setContactoAgregada(boolean contactoAgregada) {
		this.contactoAgregada = contactoAgregada;
	}

}