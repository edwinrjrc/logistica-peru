/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.Persona;
import pe.com.logistica.bean.negocio.Cliente;

/**
 * @author Edwin
 *
 */
public interface ClienteDao {

	public List<Cliente> consultarPersona(Persona persona) throws SQLException;
}
