/**
 * 
 */
package pe.com.logistica.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 *
 */
public class UtilWeb {
	
	/**
	 * 
	 */
	public UtilWeb() {
		// TODO Auto-generated constructor stub
	}

	
	public static List<SelectItem> convertirSelectItem(List<BaseVO> lista){
		List<SelectItem> listaCombo = new ArrayList<SelectItem>();
		SelectItem si = null;
		if (lista != null){
			for (BaseVO baseVO : lista) {
				si = new SelectItem(obtenerObjetoCadena(baseVO), baseVO.getNombre());
				listaCombo.add(si);
			}
		}
		
		return listaCombo;
	}
	
	public static String obtenerObjetoCadena(BaseVO baseVO){
		if (baseVO.getCodigoEntero() != null){
			return baseVO.getCodigoEntero().toString();
		}
		else{
			return baseVO.getCodigoCadena();
		}
	}
	
	public static int convertirCadenaEntero(String cadena){
		try {
			if (StringUtils.isNotBlank(cadena)){
				return Integer.parseInt(cadena);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String obtenerCadenaPropertieMaestro(String llave, String maestroPropertie){
		try {
			ResourceBundle resourceMaestros = ResourceBundle.getBundle(maestroPropertie);
			
			return resourceMaestros.getString(llave);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static int obtenerEnteroPropertieMaestro(String llave, String maestroPropertie){
		try {
			ResourceBundle resourceMaestros = ResourceBundle.getBundle(maestroPropertie);
			
			return convertirCadenaEntero(resourceMaestros.getString(llave));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String obtenerCadenaBlanco(String cadena){
		if (StringUtils.isNotBlank(cadena)){
			return StringUtils.trimToEmpty(cadena);
		}
		return "";
	}
}
