/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.CorreoElectronico;
import pe.com.logistica.bean.negocio.Cliente;

/**
 * @author Edwin
 *
 */
public interface CorreoElectronicoDao {
	
	public List<CorreoElectronico> consultarCorreosXPersona(int idPersona) throws SQLException;

	List<CorreoElectronico> consultarCorreosXPersona(int idPersona,
			Connection conn) throws SQLException;

	List<Cliente> listarCorreosContactos() throws SQLException;
}
