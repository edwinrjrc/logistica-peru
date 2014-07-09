/**
 * 
 */
package pe.com.logistica.web.faces;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import pe.com.logistica.bean.negocio.PlantillaCorreo;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "plantillaCorreoMasivoMBean")
@SessionScoped()
public class PlantillaCorreoMasivoMBean extends BaseMBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3476265246805865520L;
	
	private List<PlantillaCorreo> listaPlantilla;
	
	private PlantillaCorreo plantillaCorreo;

	/**
	 * 
	 */
	public PlantillaCorreoMasivoMBean() {
		// TODO Auto-generated constructor stub
	}

	
	public void nuevaPlantilla(){
		
	}


	public void ejecutarMetodo(ActionEvent e){
		
	}
	/**
	 * @return the listaPlantilla
	 */
	public List<PlantillaCorreo> getListaPlantilla() {
		return listaPlantilla;
	}


	/**
	 * @param listaPlantilla the listaPlantilla to set
	 */
	public void setListaPlantilla(List<PlantillaCorreo> listaPlantilla) {
		this.listaPlantilla = listaPlantilla;
	}


	/**
	 * @return the plantillaCorreo
	 */
	public PlantillaCorreo getPlantillaCorreo() {
		if (plantillaCorreo == null){
			plantillaCorreo = new PlantillaCorreo();
		}
		return plantillaCorreo;
	}


	/**
	 * @param plantillaCorreo the plantillaCorreo to set
	 */
	public void setPlantillaCorreo(PlantillaCorreo plantillaCorreo) {
		this.plantillaCorreo = plantillaCorreo;
	}
}
