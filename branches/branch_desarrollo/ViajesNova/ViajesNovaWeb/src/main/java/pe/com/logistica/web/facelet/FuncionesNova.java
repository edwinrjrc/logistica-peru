/**
 * 
 */
package pe.com.logistica.web.facelet;

import org.codehaus.plexus.util.StringUtils;

/**
 * @author Edwin
 *
 */
public class FuncionesNova {

	
	public static boolean tienePermiso(String codigoOpcion, String codigoRol) {
        return (StringUtils.equals(codigoOpcion, codigoRol));
    }

	/**
	 * 
	 * @param codigoOpcion
	 * @param codigoRol
	 * @return
	 */
	public static boolean mostrarBotonesVenta1(Integer codigoRol, Integer estadoServicio, Integer tienePagos, boolean guardoComprobantes) {
		boolean resultado = (codigoRol.intValue() == 3 && estadoServicio.intValue() ==2 && tienePagos.intValue()==0);
		
		resultado = (resultado && !guardoComprobantes);
		
        return resultado;
    }
}
