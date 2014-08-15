/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.MaestroServicio;

/**
 * @author edwreb
 *
 */
public interface MaestroServicioDao {
	
	public List<MaestroServicio> listarMaestroServicios() throws SQLException;
	
	public Integer ingresarMaestroServicio(MaestroServicio servicio) throws SQLException;
	
	public boolean actualizarMaestroServicio(MaestroServicio servicio) throws SQLException;
	
	public MaestroServicio consultarMaestroServicio(int idMaestroServicio) throws SQLException;
	

}
