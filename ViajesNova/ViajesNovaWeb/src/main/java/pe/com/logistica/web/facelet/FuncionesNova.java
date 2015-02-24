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
	/**
	 * 
	 */
	public FuncionesNova() {
		System.out.println("inicia funciones");
	}
	
	public static boolean tienePermiso(String codigoOpcion, String codigoRol) {
        return (StringUtils.equals(codigoOpcion, codigoRol));
    }

}
