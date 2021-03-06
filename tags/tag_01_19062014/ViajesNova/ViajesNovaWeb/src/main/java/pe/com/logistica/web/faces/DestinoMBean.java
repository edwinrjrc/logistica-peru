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
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author Edwin
 * 
 */
@ManagedBean(name = "destinoMBean")
@SessionScoped()
public class DestinoMBean extends BaseMBean {

	private Destino destino;

	private SoporteServicio soporteServicio;

	private List<SelectItem> listaPais;
	private List<Destino> listaDestino;

	public boolean nuevoDestino;
	public boolean editarDestino;

	/**
	 * 
	 */
	public DestinoMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			soporteServicio = new SoporteServicioImpl(servletContext);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public void nuevoDestino() {
		this.setNuevoDestino(true);
		this.setEditarDestino(false);
		this.setDestino(null);
		this.setNombreFormulario("Nuevo Destino");
	}

	public void ejecutarMetodo() {
		try {
			if (this.isNuevoDestino()) {
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");
				getDestino().setUsuarioCreacion(usuario.getUsuario());
				getDestino().setIpCreacion(obtenerRequest().getRemoteAddr());
				this.soporteServicio.ingresarDestino(getDestino());
				
				this.setShowModal(true);
				this.setTipoModal("1");
				this.setMensajeModal("Destino registrado Satisfactoriamente");
			} else if (this.isEditarDestino()) {
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");
				getDestino().setUsuarioCreacion(usuario.getUsuario());
				getDestino().setIpCreacion(obtenerRequest().getRemoteAddr());
				getDestino().setUsuarioModificacion(usuario.getUsuario());
				getDestino().setIpModificacion(obtenerRequest().getRemoteAddr());
				this.soporteServicio.actualizarDestino(getDestino());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the destino
	 */
	public Destino getDestino() {
		if (destino == null) {
			destino = new Destino();
		}
		return destino;
	}

	/**
	 * @param destino
	 *            the destino to set
	 */
	public void setDestino(Destino destino) {
		this.destino = destino;
	}

	/**
	 * @return the nuevoDestino
	 */
	public boolean isNuevoDestino() {
		return nuevoDestino;
	}

	/**
	 * @param nuevoDestino
	 *            the nuevoDestino to set
	 */
	public void setNuevoDestino(boolean nuevoDestino) {
		this.nuevoDestino = nuevoDestino;
	}

	/**
	 * @return the editarDestino
	 */
	public boolean isEditarDestino() {
		return editarDestino;
	}

	/**
	 * @param editarDestino
	 *            the editarDestino to set
	 */
	public void setEditarDestino(boolean editarDestino) {
		this.editarDestino = editarDestino;
	}

	/**
	 * @return the listaPais
	 */
	public List<SelectItem> getListaPais() {
		try {
			if (listaPais == null || listaPais.isEmpty()) {
				if (getDestino().getPais().getContinente()
						.getCodigoEntero() != null){
					List<BaseVO> lista = soporteServicio
							.consultarPaises(getDestino().getPais().getContinente()
									.getCodigoEntero());
					listaPais = UtilWeb.convertirSelectItem(lista);
				}
			}

		} catch (SQLException e) {
			listaPais = new ArrayList<SelectItem>();
			e.printStackTrace();
		} catch (Exception e) {
			listaPais = new ArrayList<SelectItem>();
			e.printStackTrace();
		}
		return listaPais;
	}

	/**
	 * @param listaPais
	 *            the listaPais to set
	 */
	public void setListaPais(List<SelectItem> listaPais) {
		this.listaPais = listaPais;
	}

	/**
	 * @return the listaDestino
	 */
	public List<Destino> getListaDestino() {
		try {
			listaDestino = this.soporteServicio.listarDestinos();

			this.setShowModal(false);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaDestino;
	}

	/**
	 * @param listaDestino
	 *            the listaDestino to set
	 */
	public void setListaDestino(List<Destino> listaDestino) {
		this.listaDestino = listaDestino;
	}
}
