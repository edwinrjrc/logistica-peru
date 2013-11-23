/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Maestro;

/**
 * @author Edwin
 *
 */
public interface MaestroDao {

	public List<Maestro> listarMaestros() throws SQLException;
	
	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException;
	
	public void ingresarMaestro(Maestro maestro) throws SQLException;
	
	public void ingresarHijoMaestro(Maestro maestro) throws SQLException;

	public Maestro consultarMaestro(int id) throws SQLException;
}
