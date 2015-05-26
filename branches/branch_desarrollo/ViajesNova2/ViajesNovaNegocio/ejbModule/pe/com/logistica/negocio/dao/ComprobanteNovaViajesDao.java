/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;

import pe.com.logistica.bean.negocio.Comprobante;

/**
 * @author Edwin
 *
 */
public interface ComprobanteNovaViajesDao {
	
	public Integer registrarComprobanteAdicional(Comprobante comprobante, Connection conn) throws SQLException;
	
	public Integer listarComprobantesAdicionales(Integer idServicio) throws SQLException;
	
	public Integer eliminarComprobantesAdicionales(Integer idServicio) throws SQLException;
	
}
