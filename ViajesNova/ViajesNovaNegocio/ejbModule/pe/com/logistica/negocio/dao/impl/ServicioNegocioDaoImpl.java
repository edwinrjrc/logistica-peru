/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.negocio.dao.ServicioNegocioDao;
import pe.com.logistica.negocio.util.UtilConexion;

/**
 * @author edwreb
 *
 */
public class ServicioNegocioDaoImpl implements ServicioNegocioDao {

	/**
	 * 
	 */
	public ServicioNegocioDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ServicioNegocioDao#calcularCuota(pe.com.logistica.bean.negocio.ServicioAgencia)
	 */
	@Override
	public BigDecimal calcularCuota(ServicioAgencia servicioAgencia) throws SQLException {
		BigDecimal resultado = BigDecimal.ZERO;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_calcularcuota(?,?,?) }";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.DECIMAL);
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotalServicios());
			cs.setBigDecimal(i++, UtilParse.parseIntABigDecimal(servicioAgencia.getNroCuotas()));
			cs.setBigDecimal(i++, servicioAgencia.getTea());
			cs.execute();
			
			resultado = cs.getBigDecimal(1);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return resultado;
	}

}
