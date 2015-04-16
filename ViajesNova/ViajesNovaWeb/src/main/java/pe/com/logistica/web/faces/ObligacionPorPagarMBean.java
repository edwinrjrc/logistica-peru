/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "obligacionPorPagarMBean")
@SessionScoped()
public class ObligacionPorPagarMBean extends BaseMBean {
	
	private final static Logger logger = Logger
			.getLogger(ObligacionPorPagarMBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -6007823264843587230L;
	
	private Comprobante comprobante;
	private Comprobante comprobanteBusqueda;
	private Proveedor proveedorBusqueda;
	
	private List<Comprobante> listaComprobantes;
	private List<Proveedor> listadoProveedores;
	
	private boolean nuevaObligacion;
	private boolean editarObligacion;
	private boolean consultoProveedor;
	private boolean buscoObligaciones;
	private boolean busquedaProveedor;

	private NegocioServicio negocioServicio;
	/**
	 * 
	 */
	public ObligacionPorPagarMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			negocioServicio = new NegocioServicioImpl(servletContext);

		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void buscar(){
		try {
			this.setListaComprobantes(this.negocioServicio.listarObligacionXPagar(getComprobanteBusqueda()));
			
			this.setBuscoObligaciones(true);
		} catch (SQLException e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			e.printStackTrace();
		} catch (Exception e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			e.printStackTrace();
		}
	}
	
	public void nuevaObligacion(){
		this.setNombreFormulario("Nueva Obligacion por Pagar");
		this.setNuevaObligacion(true);
		this.setEditarObligacion(false);
		this.setConsultoProveedor(true);
		this.setComprobante(null);
		this.setListadoProveedores(null);
		this.setBusquedaProveedor(false);
	}
	
	public void ejecutarMetodo(){
		if (validarObligacion()){
			try {
				if (this.isNuevaObligacion()){
					HttpSession session = obtenerSession(false);
					
					Usuario usuario = (Usuario) session.getAttribute(USUARIO_SESSION);
					getComprobante().setUsuarioCreacion(usuario.getUsuario());
					getComprobante().setIpCreacion(obtenerRequest().getRemoteAddr());
					
					this.setShowModal(this.negocioServicio.registrarObligacionXPagar(getComprobante()));
					this.setMensajeModal("Obligación registrados satisfactoriamente");
					this.setTipoModal(TIPO_MODAL_EXITO);
				}
				
				this.setBuscoObligaciones(false);
			} catch (SQLException e) {
				this.setShowModal(true);
				this.setMensajeModal(e.getMessage());
				this.setTipoModal(TIPO_MODAL_ERROR);
				e.printStackTrace();
			} catch (Exception e) {
				this.setShowModal(true);
				this.setMensajeModal(e.getMessage());
				this.setTipoModal(TIPO_MODAL_ERROR);
				e.printStackTrace();
			}
		}
	}
	
	
	private boolean validarObligacion() {
		boolean resultado = true;
		String idFormulario = "idFormObligacion";
		Date fechaHoy = UtilWeb.fechaHoy();
		
		if (StringUtils.isBlank(this.getComprobante().getProveedor().getNombres())){
			this.agregarMensaje(idFormulario+":idTxtProveedor", "Seleccione el proveedor", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getComprobante().getFechaComprobante().after(fechaHoy) || this.getComprobante().getFechaComprobante().equals(fechaHoy)){
			this.agregarMensaje(idFormulario+":idFecComprobante", "La fecha de comprobante no debe ser mayor a la fecha de hoy", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getComprobante().getFechaPago().before(fechaHoy) || this.getComprobante().getFechaPago().equals(fechaHoy)){
			this.agregarMensaje(idFormulario+":idFecPago", "La fecha de pago debe ser mayor a la fecha de hoy", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		
		return resultado;
	}
	
	public void buscarProveedor(){
		try {
			this.setListadoProveedores(this.negocioServicio
					.buscarProveedor(getProveedorBusqueda()));
			
			this.setConsultoProveedor(true);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void seleccionarProveedor(){
		for (Proveedor proveedor : this.listadoProveedores){
			if (proveedor.getCodigoEntero().equals(proveedor.getCodigoSeleccionado())){
				if (this.isBusquedaProveedor()){
					this.getComprobanteBusqueda().setProveedor(proveedor);
					break;
				}
				else{
					this.getComprobante().setProveedor(proveedor);
					break;
				}
			}
		}
	}
	
	public void defineSalida(){
		this.setBusquedaProveedor(true);
	}

	/**
	 * @return the comprobante
	 */
	public Comprobante getComprobante() {
		if (comprobante == null){
			comprobante = new Comprobante();
		}
		return comprobante;
	}

	/**
	 * @param comprobante the comprobante to set
	 */
	public void setComprobante(Comprobante comprobante) {
		this.comprobante = comprobante;
	}

	/**
	 * @return the comprobanteBusqueda
	 */
	public Comprobante getComprobanteBusqueda() {
		if (comprobanteBusqueda == null){
			comprobanteBusqueda = new Comprobante();
		}
		return comprobanteBusqueda;
	}

	/**
	 * @param comprobanteBusqueda the comprobanteBusqueda to set
	 */
	public void setComprobanteBusqueda(Comprobante comprobanteBusqueda) {
		this.comprobanteBusqueda = comprobanteBusqueda;
	}

	/**
	 * @return the listaComprobantes
	 */
	public List<Comprobante> getListaComprobantes() {
		this.setShowModal(false);
		if (listaComprobantes == null || !this.isBuscoObligaciones()){
			try {
				this.setListaComprobantes(this.negocioServicio.listarObligacionXPagar(getComprobanteBusqueda()));
				
				this.setBuscoObligaciones(true);
				
			} catch (SQLException e) {
				this.setShowModal(true);
				this.setMensajeModal(e.getMessage());
				this.setTipoModal(TIPO_MODAL_ERROR);
				e.printStackTrace();
			} catch (Exception e) {
				this.setShowModal(true);
				this.setMensajeModal(e.getMessage());
				this.setTipoModal(TIPO_MODAL_ERROR);
				e.printStackTrace();
			}
		}
		return listaComprobantes;
	}

	/**
	 * @param listaComprobantes the listaComprobantes to set
	 */
	public void setListaComprobantes(List<Comprobante> listaComprobantes) {
		this.listaComprobantes = listaComprobantes;
	}

	/**
	 * @return the nuevaObligacion
	 */
	public boolean isNuevaObligacion() {
		return nuevaObligacion;
	}

	/**
	 * @param nuevaObligacion the nuevaObligacion to set
	 */
	public void setNuevaObligacion(boolean nuevaObligacion) {
		this.nuevaObligacion = nuevaObligacion;
	}

	/**
	 * @return the editarObligacion
	 */
	public boolean isEditarObligacion() {
		return editarObligacion;
	}

	/**
	 * @param editarObligacion the editarObligacion to set
	 */
	public void setEditarObligacion(boolean editarObligacion) {
		this.editarObligacion = editarObligacion;
	}

	/**
	 * @return the proveedorBusqueda
	 */
	public Proveedor getProveedorBusqueda() {
		if (proveedorBusqueda == null){
			proveedorBusqueda = new Proveedor();
		}
		return proveedorBusqueda;
	}

	/**
	 * @param proveedorBusqueda the proveedorBusqueda to set
	 */
	public void setProveedorBusqueda(Proveedor proveedorBusqueda) {
		this.proveedorBusqueda = proveedorBusqueda;
	}

	/**
	 * @return the listadoProveedores
	 */
	public List<Proveedor> getListadoProveedores() {
		try {
			//if (!this.isConsultoProveedor()){
				listadoProveedores = this.negocioServicio
						.buscarProveedor(getProveedorBusqueda());
			//}
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listadoProveedores;
	}

	/**
	 * @param listadoProveedores the listadoProveedores to set
	 */
	public void setListadoProveedores(List<Proveedor> listadoProveedores) {
		this.listadoProveedores = listadoProveedores;
	}

	/**
	 * @return the consultoProveedor
	 */
	public boolean isConsultoProveedor() {
		return consultoProveedor;
	}

	/**
	 * @param consultoProveedor the consultoProveedor to set
	 */
	public void setConsultoProveedor(boolean consultoProveedor) {
		this.consultoProveedor = consultoProveedor;
	}

	/**
	 * @return the buscoObligaciones
	 */
	public boolean isBuscoObligaciones() {
		return buscoObligaciones;
	}

	/**
	 * @param buscoObligaciones the buscoObligaciones to set
	 */
	public void setBuscoObligaciones(boolean buscoObligaciones) {
		this.buscoObligaciones = buscoObligaciones;
	}

	/**
	 * @return the busquedaProveedor
	 */
	public boolean isBusquedaProveedor() {
		return busquedaProveedor;
	}

	/**
	 * @param busquedaProveedor the busquedaProveedor to set
	 */
	public void setBusquedaProveedor(boolean busquedaProveedor) {
		this.busquedaProveedor = busquedaProveedor;
	}

}
