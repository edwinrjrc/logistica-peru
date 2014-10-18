/**
 * 
 */
package pe.com.logistica.web.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static int obtenerLongitud(String cadena){
		if (StringUtils.isNotBlank(cadena)){
			return cadena.length();
		}
		return 0;
	}
	
	public static String diaHoy(){
		Calendar cal = Calendar.getInstance();
		
		switch (cal.get(Calendar.DAY_OF_WEEK)){
		case 2:
			return "Lunes";
		case 3:
			return "Martes";
		case 4:
			return "Miercoles";
		case 5:
			return "Jueves";
		case 6:
			return "Viernes";
		case 7:
			return "Sabado";
		case 1:
			return "Domingo";
		}
		
		return "";
	}
	
	public static String mesHoy(){
		Calendar cal = Calendar.getInstance();
		
		switch (cal.get(Calendar.MONTH)+1){
		case 1:
			return "Enero";
		case 2:
			return "Febrero";
		case 3:
			return "Marzo";
		case 4:
			return "Abril";
		case 5:
			return "Mayo";
		case 6:
			return "Junio";
		case 7:
			return "Julio";
		case 8:
			return "Agosto";
		case 9:
			return "Septiembre";
		case 10:
			return "Octubre";
		case 11:
			return "Noviembre";
		case 12:
			return "Diciembre";
		}
		
		return "";
	}
	
	public static boolean validaEnteroEsNuloOCero(Integer numero){
		try {
			return (numero == null || numero.intValue()==0);
			
		} catch (Exception e) {
			System.out.println("Error validacion numero cero o nullo ::"+e.getMessage());
		}
		
		return false;
	}
	
	public static boolean validarCorreo(String email){
		try {
			String patternEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern pattern = Pattern.compile(patternEmail);
			Matcher matcher = pattern.matcher(email);
			return matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return false;
	}
}
