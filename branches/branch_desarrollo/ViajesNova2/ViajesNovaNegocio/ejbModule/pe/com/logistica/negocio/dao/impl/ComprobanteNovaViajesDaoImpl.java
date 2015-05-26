/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.negocio.dao.ComprobanteNovaViajesDao;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class ComprobanteNovaViajesDaoImpl implements ComprobanteNovaViajesDao {

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ComprobanteNovaViajesDao#registrarComprobanteAdicional(pe.com.logistica.bean.negocio.Comprobante, java.sql.Connection)
	 */
	@Override
	public Integer registrarComprobanteAdicional(Comprobante comprobante,
			Connection conn) throws SQLException {
		Integer resultado = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarcomprobanteadicional(?,?,?,?,?,?,?,?,?,?)}";
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, comprobante.getIdServicio().intValue());
			cs.setInt(i++, comprobante.getTipoComprobante().getCodigoEntero().intValue());
			cs.setString(i++, comprobante.getNumeroComprobante());
			cs.setInt(i++, comprobante.getTitular().getCodigoEntero().intValue());
			cs.setString(i++, comprobante.getDetalleTextoComprobante());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(comprobante.getFechaComprobante()));
			cs.setBigDecimal(i++, comprobante.getTotalIGV());
			cs.setBigDecimal(i++, comprobante.getTotalComprobante());
			
			cs.setString(i++, comprobante.getUsuarioCreacion());
			cs.setString(i++, comprobante.getIpCreacion());
			
			cs.execute();
			
			resultado = cs.getInt(1);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		return resultado;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ComprobanteNovaViajesDao#listarComprobantesAdicionales(java.lang.Integer)
	 */
	@Override
	public Integer listarComprobantesAdicionales(Integer idServicio)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ComprobanteNovaViajesDao#eliminarComprobantesAdicionales(java.lang.Integer)
	 */
	@Override
	public Integer eliminarComprobantesAdicionales(Integer idServicio)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
