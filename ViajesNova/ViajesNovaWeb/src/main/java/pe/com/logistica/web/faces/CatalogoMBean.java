/**
 * 
 */
package pe.com.logistica.web.faces;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.web.servicio.SeguridadServicio;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.impl.SeguridadServicioImpl;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author Edwin
 * 
 */
@ManagedBean(name = "catalogoMBean")
@SessionScoped()
public class CatalogoMBean implements Serializable{
	
	private final static Logger logger = Logger.getLogger(CatalogoMBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -571289965929551249L;
	
	
	private List<SelectItem> catalogoRoles;
	private List<SelectItem> catalogoTipoDocumento;
	private List<SelectItem> catalogoRubro;
	private List<SelectItem> catalogoArea;
	private List<SelectItem> catalogoVias;
	private List<SelectItem> catalogoDepartamento;
	private List<SelectItem> catalogoOperadoraMovil;
	private List<SelectItem> catalogoEstadoCivil;
	private List<SelectItem> catalogoContinente;
	private List<SelectItem> catalogoTipoDestino;
	private List<SelectItem> catalogoDestino;
	private List<SelectItem> catalogoTipoServicio;
	private List<SelectItem> catalogoFormaPago;
	private List<SelectItem> catalogoVendedores;

	private SeguridadServicio seguridadServicio;
	private SoporteServicio soporteServicio;

	public CatalogoMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			seguridadServicio = new SeguridadServicioImpl(servletContext);
			soporteServicio = new SoporteServicioImpl(servletContext);

		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @return the catalogoRoles
	 */
	public List<SelectItem> getCatalogoRoles() {
		List<BaseVO> lista = seguridadServicio.listarRoles();
		catalogoRoles = UtilWeb.convertirSelectItem(lista);
		return catalogoRoles;
	}

	/**
	 * @param catalogoRoles
	 *            the catalogoRoles to set
	 */
	public void setCatalogoRoles(List<SelectItem> catalogoRoles) {
		this.catalogoRoles = catalogoRoles;
	}

	/**
	 * @return the catalogoTipoDocumento
	 */
	public List<SelectItem> getCatalogoTipoDocumento() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroTipoDocumento", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoTipoDocumento = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoDocumento;
	}

	/**
	 * @param catalogoTipoDocumento
	 *            the catalogoTipoDocumento to set
	 */
	public void setCatalogoTipoDocumento(List<SelectItem> catalogoTipoDocumento) {
		this.catalogoTipoDocumento = catalogoTipoDocumento;
	}

	/**
	 * @return the catalogoRubro
	 */
	public List<SelectItem> getCatalogoRubro() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro("maestroRubo",
				"aplicacionDatos");
		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoRubro = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoRubro;
	}

	/**
	 * @param catalogoRubro
	 *            the catalogoRubro to set
	 */
	public void setCatalogoRubro(List<SelectItem> catalogoRubro) {
		this.catalogoRubro = catalogoRubro;
	}

	/**
	 * @return the catalogoDepartamento
	 */
	public List<SelectItem> getCatalogoDepartamento() {
		try {
			List<BaseVO> lista = soporteServicio.listarCatalogoDepartamento();
			catalogoDepartamento = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoDepartamento;
	}

	/**
	 * @param catalogoDepartamento
	 *            the catalogoDepartamento to set
	 */
	public void setCatalogoDepartamento(List<SelectItem> catalogoDepartamento) {
		this.catalogoDepartamento = catalogoDepartamento;
	}

	/**
	 * @return the catalogoVias
	 */
	public List<SelectItem> getCatalogoVias() {
		try {
			int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
					"maestroVias", "aplicacionDatos");
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoVias = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			catalogoVias = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoVias = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoVias;
	}

	/**
	 * @param catalogoVias
	 *            the catalogoVias to set
	 */
	public void setCatalogoVias(List<SelectItem> catalogoVias) {
		this.catalogoVias = catalogoVias;
	}

	/**
	 * @return the catalogoArea
	 */
	public List<SelectItem> getCatalogoArea() {
		try {
			int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
					"maestroAreas", "aplicacionDatos");
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoArea = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			catalogoArea = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoArea = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoArea;
	}

	/**
	 * @param catalogoArea
	 *            the catalogoArea to set
	 */
	public void setCatalogoArea(List<SelectItem> catalogoArea) {
		this.catalogoArea = catalogoArea;
	}

	/**
	 * @return the catalogoOperadoraMovil
	 */
	public List<SelectItem> getCatalogoOperadoraMovil() {
		try {
			int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
					"maestroOperadoraMovil", "aplicacionDatos");
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoOperadoraMovil = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			catalogoOperadoraMovil = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoOperadoraMovil = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoOperadoraMovil;
	}

	/**
	 * @param catalogoOperadoraMovil
	 *            the catalogoOperadoraMovil to set
	 */
	public void setCatalogoOperadoraMovil(
			List<SelectItem> catalogoOperadoraMovil) {
		this.catalogoOperadoraMovil = catalogoOperadoraMovil;
	}

	/**
	 * @return the catalogoEstadoCivil
	 */
	public List<SelectItem> getCatalogoEstadoCivil() {
		try {
			int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
					"maestroEstadoCivil", "aplicacionDatos");
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoEstadoCivil = UtilWeb.convertirSelectItem(lista);
		} catch (SQLException e) {
			catalogoEstadoCivil = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoEstadoCivil = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoEstadoCivil;
	}

	/**
	 * @param catalogoEstadoCivil
	 *            the catalogoEstadoCivil to set
	 */
	public void setCatalogoEstadoCivil(List<SelectItem> catalogoEstadoCivil) {
		this.catalogoEstadoCivil = catalogoEstadoCivil;
	}

	/**
	 * @return the catalogoContinente
	 */
	public List<SelectItem> getCatalogoContinente() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroContinente", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoContinente = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoContinente;
	}

	/**
	 * @param catalogoContinente
	 *            the catalogoContinente to set
	 */
	public void setCatalogoContinente(List<SelectItem> catalogoContinente) {
		this.catalogoContinente = catalogoContinente;
	}

	/**
	 * @return the catalogoTipoDestino
	 */
	public List<SelectItem> getCatalogoTipoDestino() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroTipoDestino", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoTipoDestino = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoDestino;
	}

	/**
	 * @param catalogoTipoDestino
	 *            the catalogoTipoDestino to set
	 */
	public void setCatalogoTipoDestino(List<SelectItem> catalogoTipoDestino) {
		this.catalogoTipoDestino = catalogoTipoDestino;
	}

	/**
	 * @return the catalogoDestino
	 */
	public List<SelectItem> getCatalogoDestino() {
		try {
			catalogoDestino = new ArrayList<SelectItem>();
			List<Destino> listaDestino = this.soporteServicio.listarDestinos();
			SelectItem si = null;
			for (Destino destino : listaDestino) {
				si = new SelectItem();
				si.setValue(destino.getCodigoEntero());
				String descripcionCompleto = destino.getDescripcion()+"("+destino.getCodigoIATA()+")"; 
				si.setLabel(descripcionCompleto);
				catalogoDestino.add(si);
			}

		} catch (SQLException e) {
			catalogoDestino = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoDestino = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoDestino;
	}

	/**
	 * @param catalogoDestino the catalogoDestino to set
	 */
	public void setCatalogoDestino(List<SelectItem> catalogoDestino) {
		this.catalogoDestino = catalogoDestino;
	}

	/**
	 * @return the catalogoTipoServicio
	 */
	public List<SelectItem> getCatalogoTipoServicio() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroTipoServicios", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoTipoServicio = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoTipoServicio;
	}

	/**
	 * @param catalogoTipoServicio the catalogoTipoServicio to set
	 */
	public void setCatalogoTipoServicio(List<SelectItem> catalogoTipoServicio) {
		this.catalogoTipoServicio = catalogoTipoServicio;
	}

	/**
	 * @return the catalogoFormaPago
	 */
	public List<SelectItem> getCatalogoFormaPago() {
		int idmaestro = UtilWeb.obtenerEnteroPropertieMaestro(
				"maestroFormaPago", "aplicacionDatos");

		try {
			List<BaseVO> lista = soporteServicio
					.listarCatalogoMaestro(idmaestro);
			catalogoFormaPago = UtilWeb.convertirSelectItem(lista);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return catalogoFormaPago;
	}

	/**
	 * @param catalogoFormaPago the catalogoFormaPago to set
	 */
	public void setCatalogoFormaPago(List<SelectItem> catalogoFormaPago) {
		this.catalogoFormaPago = catalogoFormaPago;
	}

	/**
	 * @return the catalogoVendedores
	 */
	public List<SelectItem> getCatalogoVendedores() {
		try {
			catalogoVendedores = new ArrayList<SelectItem>();
			List<Usuario> listaVendedores = this.seguridadServicio.listarVendedores();
			SelectItem si = null;
			for (Usuario usuario : listaVendedores) {
				si = new SelectItem();
				si.setValue(usuario.getCodigoEntero());
				String descripcionCompleto = usuario.getNombreCompleto();
				si.setLabel(descripcionCompleto);
				catalogoVendedores.add(si);
			}

		} catch (SQLException e) {
			catalogoVendedores = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			catalogoVendedores = new ArrayList<SelectItem>();
			logger.error(e.getMessage(), e);
		}
		return catalogoVendedores;
	}

	/**
	 * @param catalogoVendedores the catalogoVendedores to set
	 */
	public void setCatalogoVendedores(List<SelectItem> catalogoVendedores) {
		this.catalogoVendedores = catalogoVendedores;
	}

}
