/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.math.BigDecimal;
import java.sql.SQLException;

import pe.com.logistica.bean.negocio.ServicioAgencia;

/**
 * @author edwreb
 *
 */
public interface ServicioNegocioDao {

	public BigDecimal calcularCuota(ServicioAgencia servicioAgencia) throws SQLException;
}
