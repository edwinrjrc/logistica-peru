/**
 * 
 */
package pe.com.logistica.web.faces;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.web.servicio.SeguridadServicio;
import pe.com.logistica.web.servicio.impl.SeguridadServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author Edwin
 *
 */
@ManagedBean(name="catalogoMBean")
public class CatalogoMBean {

	private List<SelectItem> catalogoRoles;
	
	private SeguridadServicio seguridadServicio;
	
	public CatalogoMBean() {
		try {
			seguridadServicio = new SeguridadServicioImpl();
		} catch (NamingException e) {
			e.printStackTrace();
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
	 * @param catalogoRoles the catalogoRoles to set
	 */
	public void setCatalogoRoles(List<SelectItem> catalogoRoles) {
		this.catalogoRoles = catalogoRoles;
	}

}
