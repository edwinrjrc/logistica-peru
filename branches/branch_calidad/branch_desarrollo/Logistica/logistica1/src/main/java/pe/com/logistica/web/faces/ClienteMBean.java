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
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.base.CorreoElectronico;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.bean.negocio.Ubigeo;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.NoEnvioDatoException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author Edwin
 *
 */

@ManagedBean(name = "clienteMBean")
@SessionScoped()
public class ClienteMBean extends BaseMBean {
	
	private List<Cliente> listaClientes;
	
	private Cliente cliente;
	private Cliente clienteBusqueda;
	private Direccion direccion;
	private Contacto contacto;
	
	private boolean nuevoCliente;
	private boolean editarCliente;
	private boolean nuevaDireccion;
	private boolean editarDireccion;
	private boolean nuevoContacto;
	private boolean editarContacto;
	private boolean direccionAgregada;
	private boolean contactoAgregada;
	private boolean busquedaRealizada;

	private String nombreFormulario;
	private String nombreFormularioDireccion;
	private String nombreFormularioContacto;
	private String pestanaActiva = "idFC01";

	private List<SelectItem> listaProvincia;
	private List<SelectItem> listaDistrito;

	private SoporteServicio soporteServicio;
	private NegocioServicio negocioServicio;

	/**
	 * 
	 */
	public ClienteMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			soporteServicio = new SoporteServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public String inicioAdministrador(){
		this.setBusquedaRealizada(false);
		
		System.out.println("codigo :"+this.getCliente().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero());
		
		return "irAdministrarCliente";
	}
	
	
	public void buscarCliente(){
		try {
			this.setShowModal(false);
			this.setListaClientes(this.negocioServicio.buscarCliente(getClienteBusqueda()));
			this.setBusquedaRealizada(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void ejecutarMetodo(ActionEvent e){
		try {
			this.setShowModal(false);
			int tipoDocRUC = UtilWeb.obtenerEnteroPropertieMaestro(
					"tipoDocumentoRUC", "aplicacionDatos");
			this.setListaClientes(null);
			
			if (validarCliente(e)) {
				if (validarDireccionCliente(e)) {
					if (validarContactoCliente(e)) {
						if (validarTelefonoCliente()){
							if (this.isNuevoCliente()){
								HttpSession session = obtenerSession(false);
								Usuario usuario = (Usuario) session
										.getAttribute("usuarioSession");
								getCliente().setUsuarioCreacion(
										usuario.getUsuario());
								getCliente().setIpCreacion(
										obtenerRequest().getRemoteAddr());
								if (tipoDocRUC == getCliente().getDocumentoIdentidad()
										.getTipoDocumento().getCodigoEntero().intValue()) {
									getCliente().setNombres(getCliente().getRazonSocial());
								}
								
								this.setShowModal(negocioServicio
										.registrarCliente(getCliente()));
								this.setTipoModal("1");
								this.setMensajeModal("Cliente registrado Satisfactoriamente");
							}
							else if (this.isEditarCliente()){
								HttpSession session = obtenerSession(false);
								Usuario usuario = (Usuario) session
										.getAttribute("usuarioSession");
								getCliente().setUsuarioModificacion(
										usuario.getUsuario());
								getCliente().setIpModificacion(
										obtenerRequest().getRemoteAddr());
								if (tipoDocRUC == getCliente().getDocumentoIdentidad()
										.getTipoDocumento().getCodigoEntero().intValue()) {
									getCliente().setNombres(getCliente().getRazonSocial());
								}
								
								this.setShowModal(negocioServicio
										.actualizarCliente(getCliente()));
								this.setTipoModal("1");
								this.setMensajeModal("Cliente actualizado Satisfactoriamente");
							}
						}
						else {
							this.setShowModal(true);
							this.setTipoModal("2");
							this.setMensajeModal("Debe ingresar por lo menos un telefono, en la direccion o el contacto");
						}
						
					} else {
						this.setShowModal(true);
						this.setTipoModal("2");
						this.setMensajeModal("No se ha agregado ningun contacto para el cliente, para cliente personal natural registre como contacto la mismo persona");
					}
				} else {
					this.setShowModal(true);
					this.setTipoModal("2");
					this.setMensajeModal("No se ha agregado ninguna dirección al proveedor");
				}
			}
		} catch (NoEnvioDatoException ex){
			this.setShowModal(true);
			this.setTipoModal("2");
			this.setMensajeModal(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			this.setShowModal(true);
			this.setTipoModal("2");
			this.setMensajeModal(ex.getMessage());
		}
	}
	
	private boolean validarTelefonoCliente() {
		boolean resultado = false;
		
		List<Direccion> listaDire = this.getCliente().getListaDirecciones();
		for (Direccion direccion : listaDire) {
			List<Telefono> listTelefono = direccion.getTelefonos();
			for (Telefono telefono : listTelefono) {
				if (StringUtils.isNotBlank(telefono.getNumeroTelefono())){
					resultado = true;
					break;
				}
			}
		}
		if (!resultado){
			List<Contacto> listaContacto = this.getCliente().getListaContactos();
			for (Contacto contacto : listaContacto) {
				List<Telefono> listTelefono = contacto.getListaTelefonos();
				for (Telefono telefono : listTelefono) {
					if (StringUtils.isNotBlank(telefono.getNumeroTelefono())){
						resultado = true;
						break;
					}
				}
			}
		}
		
		return resultado;
	}


	private boolean validarContactoCliente(ActionEvent e) {
		boolean resultado = true;

		resultado = !(this.getCliente().getListaContactos().isEmpty());
		

		return resultado;
	}


	private boolean validarDireccionCliente(ActionEvent e) throws NoEnvioDatoException {
		boolean resultado = true;

		resultado = !(this.getCliente().getListaDirecciones().isEmpty());
		
		int principales = 0;
		if (resultado){
			for(Direccion direccion : this.getCliente().getListaDirecciones()){
				if (direccion.isPrincipal()){
					principales++;
				}
			}
		}
		
		if (principales == 0){
			throw new NoEnvioDatoException("1010","Debe ingresar una direccion principal");
		}

		return resultado;
	}


	private boolean validarCliente(ActionEvent e) {
		boolean resultado = true;
		String idFormulario = "idFormProveedor";
		int tipoDocDNI = UtilWeb.obtenerEnteroPropertieMaestro(
				"tipoDocumentoDNI", "aplicacionDatos");
		int tipoDocCE = UtilWeb.obtenerEnteroPropertieMaestro(
				"tipoDocumentoCE", "aplicacionDatos");
		int tipoDocRUC = UtilWeb.obtenerEnteroPropertieMaestro(
				"tipoDocumentoRUC", "aplicacionDatos");

		if (tipoDocDNI == getCliente().getDocumentoIdentidad()
				.getTipoDocumento().getCodigoEntero().intValue()
				|| tipoDocCE == getCliente().getDocumentoIdentidad()
						.getTipoDocumento().getCodigoEntero().intValue()) {
			if (StringUtils.isBlank(getCliente().getApellidoMaterno())) {
				this.agregarMensaje(idFormulario + ":idApeMatPro",
						"Ingrese el apellido materno", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (StringUtils.isBlank(getCliente().getApellidoPaterno())) {
				this.agregarMensaje(idFormulario + ":idApePatPro",
						"Ingrese el apellido paterno", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (StringUtils.isBlank(getCliente().getApellidoPaterno())) {
				this.agregarMensaje(idFormulario + ":idApePatPro",
						"Ingrese el apellido paterno", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (StringUtils.isBlank(getCliente().getNombres())) {
				this.agregarMensaje(idFormulario + ":idNomPro",
						"Ingrese los nombres", "", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
		}
		if (tipoDocRUC == getCliente().getDocumentoIdentidad()
				.getTipoDocumento().getCodigoEntero().intValue()) {
			if (StringUtils.isBlank(getCliente().getRazonSocial())) {
				this.agregarMensaje(idFormulario + ":idRazSocPro",
						"Ingrese la razon social", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
		}
		
		return resultado;
	}


	public void consultarCliente(int idcliente){
		try {
			this.setCliente(negocioServicio.consultarClienteCompleto(idcliente));
			this.setNuevoCliente(false);
			this.setEditarCliente(true);
			this.setNombreFormulario("Editar Cliente");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void nuevoCliente(){
		this.setNuevoCliente(true);
		this.setEditarCliente(false);
		this.setCliente(null);
		this.setNombreFormulario("Nuevo Cliente");
	}
	public void nuevaDireccion(){
		this.setNombreFormularioDireccion("Nueva Dirección");
		this.setNuevaDireccion(true);
		this.setEditarDireccion(false);
		this.setListaDistrito(null);
		this.setListaProvincia(null);
		this.setDireccion(null);
		this.setDireccionAgregada(false);
	}
	public void agregarDireccion(ActionEvent e){
		try {
			this.setDireccionAgregada(false);
			if (this.validarDireccion(e)) {
				if (this.isNuevaDireccion()){
					this.getCliente().getListaDirecciones()
					.add(negocioServicio.agregarDireccion(getDireccion()));
					this.setDireccionAgregada(true);
				}
				else{
					this.getCliente().getListaDirecciones().remove(getDireccion());
					this.getCliente().getListaDirecciones()
					.add(negocioServicio.agregarDireccion(getDireccion()));
					this.setDireccionAgregada(true);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private boolean validarDireccion(ActionEvent e) {
		boolean resultado = true;
		String idFormulario = "idFDPr";
		if (getDireccion().getVia().getCodigoEntero() == null) {
			this.agregarMensaje(idFormulario + ":idFDSelTipoVia",
					"Seleccione el tipo de via", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
			this.setPestanaActiva("idFC01");
		} else if (getDireccion().getVia().getCodigoEntero().intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idFDSelTipoVia",
					"Seleccione el tipo de via", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
			this.setPestanaActiva("idFD01");
		}
		if (StringUtils.isBlank(getDireccion().getNombreVia())) {
			this.agregarMensaje(idFormulario + ":idFDInNomVia",
					"Ingrese el nombre de via", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
			this.setPestanaActiva("idFD01");
		}
		if (StringUtils.isBlank(getDireccion().getUbigeo().getDepartamento()
				.getCodigoCadena())) {
			this.agregarMensaje(idFormulario + ":idDepartamentoDireccion",
					"Seleccione el departamento", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
			this.setPestanaActiva("idFD01");
		}
		if (StringUtils.isBlank(getDireccion().getUbigeo().getProvincia()
				.getCodigoCadena())) {
			this.agregarMensaje(idFormulario + ":idProvinciaDireccion",
					"Seleccione la provincia", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
			this.setPestanaActiva("idFD01");
		}
		if (StringUtils.isBlank(getDireccion().getUbigeo().getDistrito()
				.getCodigoCadena())) {
			this.agregarMensaje(idFormulario + ":idDistritoDireccion",
					"Seleccione el distrito", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
			this.setPestanaActiva("idFD01");
		}
		if (resultado){
			List<Telefono> listTelefonos = getDireccion().getTelefonos();
			if (!listTelefonos.isEmpty()){
				for (Telefono telefono : listTelefonos) {
					if (StringUtils.isBlank(telefono.getNumeroTelefono())){
						this.agregarMensaje(idFormulario + ":idTablaTelDir",
								"Complete los telefonos vacios", "",
								FacesMessage.SEVERITY_ERROR);
						resultado = false;
						this.setPestanaActiva("idFD02");
						break;
					}
				}
			}
		}

		return resultado;
	}


	public void buscarProvincias(){
		this.setListaProvincia(null);
		this.setListaDistrito(null);
		this.getDireccion().getUbigeo().setProvincia(null);
		this.getDireccion().getUbigeo().setDistrito(null);
	}
	public void buscarDistrito(){
		this.setListaDistrito(null);
		this.getDireccion().getUbigeo().setDistrito(null);
	}
	public void eliminarTelefono(Telefono telefono){
		this.getDireccion().getTelefonos().remove(telefono);
		
	}
	public void agregarTelefonoDireccion(){
		Telefono telefono = new Telefono();
		telefono.setId(this.getDireccion().getTelefonos().size() + 1);
		this.getDireccion().getTelefonos().add(telefono);
	}
	
	public void editarDireccionCliente(Direccion direccion){
		this.setNombreFormularioDireccion("Editar Dirección");
		this.setNuevaDireccion(false);
		this.setEditarDireccion(true);
		this.setDireccion(direccion);
	}
	
	public void eliminarDireccionCliente(Direccion direccion){
		this.getCliente().getListaDirecciones().remove(direccion);
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
				getContacto().setUsuarioModificacion(usuario.getUsuario());
				getContacto().setIpModificacion(obtenerRequest().getRemoteAddr());
				if (this.isEditarContacto()){
					this.getCliente().getListaContactos().remove(getContacto());
				}
				
				this.getCliente().getListaContactos().add(negocioServicio.agregarContacto(getContacto()));
				this.setContactoAgregada(true);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean validarContacto(ActionEvent e) {
		boolean resultado = true;
		String idFormulario = "idFCPr";
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
			this.setPestanaActiva("idFC01");
		}
		if (StringUtils.isBlank(getContacto().getApellidoMaterno())) {
			this.agregarMensaje(idFormulario + ":idApeMat",
					"Ingrese el apellido materno", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
			this.setPestanaActiva("idFC01");
		}
		if (StringUtils.isBlank(getContacto().getNombres())) {
			this.agregarMensaje(idFormulario + ":idForProNom",
					"Ingrese los nombres", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
			this.setPestanaActiva("idFC01");
		}
		if (getContacto().getArea().getCodigoEntero() == null) {
			this.agregarMensaje(idFormulario + ":idSelAreCon",
					"Seleccione el area del contacto", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
			this.setPestanaActiva("idFC01");
		}
		if (StringUtils.isNotBlank(getContacto().getDocumentoIdentidad().getNumeroDocumento())){
			if (getContacto().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero() != null){
				if (getContacto().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero().intValue() == tipoDocDNI){
					if (getContacto().getDocumentoIdentidad().getNumeroDocumento().length() != 8){
						this.agregarMensaje(idFormulario + ":idNumDoc",
								"El número de documento debe ser de 8 caracteres", "",
								FacesMessage.SEVERITY_ERROR);
						resultado = false;
						this.setPestanaActiva("idFC01");
					}
				}
				if (getContacto().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero().intValue() == tipoDocCE){
					if (getContacto().getDocumentoIdentidad().getNumeroDocumento().length() != 9){
						this.agregarMensaje(idFormulario + ":idNumDoc",
								"El número de documento debe ser de 9 caracteres", "",
								FacesMessage.SEVERITY_ERROR);
						resultado = false;
						this.setPestanaActiva("idFC01");
					}
				}
				if (getContacto().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero().intValue() == tipoDocRUC){
					if (getContacto().getDocumentoIdentidad().getNumeroDocumento().length() != 11){
						this.agregarMensaje(idFormulario + ":idNumDoc",
								"El número de documento debe ser de 11 caracteres", "",
								FacesMessage.SEVERITY_ERROR);
						resultado = false;
						this.setPestanaActiva("idFC01");
					}
				}
			}
		}
		if (resultado){
			List<Telefono> listTelefonos = getContacto().getListaTelefonos();
			if (!listTelefonos.isEmpty()){
				for (Telefono telefono : listTelefonos) {
					if (StringUtils.isBlank(telefono.getNumeroTelefono())){
						this.agregarMensaje(idFormulario + ":idTablaTelefonosContacto",
								"Complete los telefonos vacios", "",
								FacesMessage.SEVERITY_ERROR);
						resultado = false;
						this.setPestanaActiva("idFC02");
						break;
					}
				}
			}
		}
		
		if (resultado){
			List<CorreoElectronico> listCorreos = getContacto().getListaCorreos();
			if (!listCorreos.isEmpty()){
				for (CorreoElectronico correo : listCorreos) {
					if (StringUtils.isBlank(correo.getDireccion())){
						this.agregarMensaje(idFormulario + ":idTablaCorreoContacto",
								"Complete los correos electronicos vacios", "",
								FacesMessage.SEVERITY_ERROR);
						resultado = false;
						this.setPestanaActiva("idFC03");
						break;
					}
				}
			}
		}

		return resultado;
	}


	public void editarContactoCliente(Contacto contactoLista) {
		this.setNombreFormularioContacto("Editar Contacto");
		this.setNuevoContacto(false);
		this.setEditarContacto(true);
		this.setContacto(contactoLista);
	}

	public void eliminarContactoCliente(Contacto contactoLista) {
		this.getCliente().getListaContactos().remove(contactoLista);
	}
	
	public void agregarTelefonoContacto() {
		Telefono telefono = new Telefono();
		telefono.setId(this.getContacto().getListaTelefonos().size() + 1);
		this.getContacto().getListaTelefonos().add(telefono);
	}
	public void agregarCorreoContacto(){
		this.getContacto().getListaCorreos().add(new CorreoElectronico());
	}
	
	public void eliminarTelefonoContacto(Telefono telefono) {
		this.getContacto().getListaTelefonos().remove(telefono);
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		if (cliente == null){
			cliente = new Cliente();
		}
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
	 * @return the nuevoCliente
	 */
	public boolean isNuevoCliente() {
		return nuevoCliente;
	}

	/**
	 * @param nuevoCliente the nuevoCliente to set
	 */
	public void setNuevoCliente(boolean nuevoCliente) {
		this.nuevoCliente = nuevoCliente;
	}

	/**
	 * @return the editarCliente
	 */
	public boolean isEditarCliente() {
		return editarCliente;
	}

	/**
	 * @param editarCliente the editarCliente to set
	 */
	public void setEditarCliente(boolean editarCliente) {
		this.editarCliente = editarCliente;
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

	/**
	 * @return the direccionAgregada
	 */
	public boolean isDireccionAgregada() {
		return direccionAgregada;
	}

	/**
	 * @param direccionAgregada the direccionAgregada to set
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
	 * @param contactoAgregada the contactoAgregada to set
	 */
	public void setContactoAgregada(boolean contactoAgregada) {
		this.contactoAgregada = contactoAgregada;
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
	 * @return the pestanaActiva
	 */
	public String getPestanaActiva() {
		return pestanaActiva;
	}

	/**
	 * @param pestanaActiva the pestanaActiva to set
	 */
	public void setPestanaActiva(String pestanaActiva) {
		this.pestanaActiva = pestanaActiva;
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
	 * @param listaDistrito the listaDistrito to set
	 */
	public void setListaDistrito(List<SelectItem> listaDistrito) {
		this.listaDistrito = listaDistrito;
	}

	/**
	 * @return the listaClientes
	 */
	public List<Cliente> getListaClientes() {
		try {
			this.setShowModal(false);
			if (!this.isBusquedaRealizada()){
				listaClientes = this.negocioServicio.listarCliente();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaClientes;
	}
		
	/**
	 * @param listaClientes the listaClientes to set
	 */
	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}


	/**
	 * @return the busquedaRealizada
	 */
	public boolean isBusquedaRealizada() {
		return busquedaRealizada;
	}


	/**
	 * @param busquedaRealizada the busquedaRealizada to set
	 */
	public void setBusquedaRealizada(boolean busquedaRealizada) {
		this.busquedaRealizada = busquedaRealizada;
	}

	/**
	 * @return the clienteBusqueda
	 */
	public Cliente getClienteBusqueda() {
		if (clienteBusqueda == null){
			clienteBusqueda = new Cliente();
		}
		return clienteBusqueda;
	}

	/**
	 * @param clienteBusqueda the clienteBusqueda to set
	 */
	public void setClienteBusqueda(Cliente clienteBusqueda) {
		this.clienteBusqueda = clienteBusqueda;
	}

}
