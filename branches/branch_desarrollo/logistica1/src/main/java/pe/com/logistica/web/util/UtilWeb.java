/**
 * 
 */
package pe.com.logistica.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

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
				si = new SelectItem(baseVO.getCodigoEntero().toString(), baseVO.getNombre());
				listaCombo.add(si);
			}
		}
		
		return listaCombo;
	}
}
