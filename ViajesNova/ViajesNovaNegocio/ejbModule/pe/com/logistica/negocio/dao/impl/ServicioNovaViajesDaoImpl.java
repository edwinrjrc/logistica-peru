/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.negocio.dao.ServicioNovaViajesDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author edwreb
 *
 */
public class ServicioNovaViajesDaoImpl implements ServicioNovaViajesDao {

	/**
	 * 
	 */
	public ServicioNovaViajesDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ServicioNovaViajesDao#ingresarCabeceraServicio(pe.com.logistica.bean.negocio.ServicioAgencia)
	 */
	@Override
	public Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia)
			throws SQLException {
		Integer idservicio = 0;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_ingresarserviciocabecera(?,?,?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCliente().getCodigoEntero().intValue());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			cs.setBigDecimal(i++, programaNovios.getNovio().getCodigoEntero());
			cs.setInt(i++, programaNovios.getDestino().getCodigoEntero());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(programaNovios.getFechaBoda()));
			cs.setInt(i++, programaNovios.getMoneda().getCodigoEntero());
			cs.setBigDecimal(i++, programaNovios.getCuotaInicial());
			cs.setInt(i++, programaNovios.getNroDias());
			cs.setInt(i++, programaNovios.getNroNoches());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(programaNovios.getFechaShower()));
			if (StringUtils.isNotBlank(programaNovios.getObservaciones())){
				cs.setString(i++, programaNovios.getObservaciones());
			}
			else {
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setString(i++, programaNovios.getUsuarioCreacion());
			cs.setString(i++, programaNovios.getIpCreacion());
			cs.execute();
			
			codigoNovios = cs.getInt(1);
		} catch (SQLException e) {
			codigoNovios = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return codigoNovios;
	}

}
