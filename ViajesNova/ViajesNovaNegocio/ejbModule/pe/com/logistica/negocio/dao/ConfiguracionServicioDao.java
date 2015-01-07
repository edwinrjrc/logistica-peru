/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;

import pe.com.logistica.bean.negocio.ConfiguracionTipoServicio;

/**
 * @author Edwin
 *
 */
public interface ConfiguracionServicioDao {

	public ConfiguracionTipoServicio consultarConfiguracionServicio(Integer idTipoServicio) throws SQLException, Exception;
}
