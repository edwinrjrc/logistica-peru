/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
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

	void registroCliente(Cliente cliente, Connection conexion)
			throws SQLException;

	void actualizarPersonaAdicional(Cliente cliente, Connection conexion)
			throws SQLException;

	List<Cliente> buscarPersona(Persona persona) throws SQLException;

	public Cliente consultarCliente(int idcliente) throws SQLException;
	
	public List<Cliente> listarClientesNovios(String genero) throws SQLException;

	List<Persona> listarClientes(Persona persona) throws SQLException; 
}