/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Destino;

/**
 * @author Edwin
 *
 */
public interface DestinoDao {

	public boolean ingresarDestino(Destino destino) throws SQLException;
	
	public boolean actualizarDestino(Destino destino) throws SQLException;

	List<Destino> listarDestinos() throws SQLException;
}
