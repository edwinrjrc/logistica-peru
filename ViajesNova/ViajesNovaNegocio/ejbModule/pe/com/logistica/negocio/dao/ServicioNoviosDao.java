/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;

import pe.com.logistica.bean.negocio.ProgramaNovios;

/**
 * @author Edwin
 *
 */
public interface ServicioNoviosDao {
	
	public String registrarNovios(ProgramaNovios programaNovios) throws SQLException;

}
