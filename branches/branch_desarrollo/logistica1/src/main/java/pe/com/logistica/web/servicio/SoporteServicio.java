/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Maestro;

/**
 * @author Edwin
 * 
 */
public interface SoporteServicio {

	public List<Maestro> listarMaestros() throws SQLException;

	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException;

	public boolean ingresarMaestro(Maestro maestro) throws SQLException;

	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException;

	public Maestro consultarMaestro(int idmaestro) throws SQLException;

	public Maestro consultarHijoMaestro(Maestro hijo) throws SQLException;
	
	public boolean actualizarMaestro(Maestro hijo) throws SQLException;
}
