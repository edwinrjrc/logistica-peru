/**
 * 
 */
package pe.com.logistica.web.faces;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.ComprobanteBusqueda;

/**
 * @author EDWREB
 *
 */
@ManagedBean(name = "comprobanteMBean")
@SessionScoped()
public class ComprobanteMBean extends BaseMBean {

	private final static Logger logger = Logger.getLogger(ComprobanteMBean.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3796481899238208609L;
	
	private ComprobanteBusqueda comprobanteBusqueda;
	
	private List<Comprobante> listaComprobantes;

	/**
	 * 
	 */
	public ComprobanteMBean() {
		// TODO Auto-generated constructor stub
	}

	
	public void buscar(){
		
	}
	/**
	 * =======================================================================================================================================
	 */
	
	/**
	 * @return the comprobanteBusqueda
	 */
	public ComprobanteBusqueda getComprobanteBusqueda() {
		if (comprobanteBusqueda == null){
			comprobanteBusqueda = new ComprobanteBusqueda();
		}
		return comprobanteBusqueda;
	}

	/**
	 * @param comprobanteBusqueda the comprobanteBusqueda to set
	 */
	public void setComprobanteBusqueda(ComprobanteBusqueda comprobanteBusqueda) {
		this.comprobanteBusqueda = comprobanteBusqueda;
	}


	/**
	 * @return the listaComprobantes
	 */
	public List<Comprobante> getListaComprobantes() {
		if (listaComprobantes == null){
			listaComprobantes = new ArrayList<Comprobante>();
		}
		return listaComprobantes;
	}


	/**
	 * @param listaComprobantes the listaComprobantes to set
	 */
	public void setListaComprobantes(List<Comprobante> listaComprobantes) {
		this.listaComprobantes = listaComprobantes;
	}

}
