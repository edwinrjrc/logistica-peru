/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.negocio.dao.ServicioNoviosDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class ServicioNoviosDaoImpl implements ServicioNoviosDao {

	/**
	 * 
	 */
	public ServicioNoviosDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ServicioNoviosDao#registrarNovios(pe.com.logistica.bean.negocio.ProgramaNovios)
	 */
	@Override
	public String registrarNovios(ProgramaNovios programaNovios)
			throws SQLException {
		String codigoNovios = "";
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_ingresarservicionovios(?,?,?,?,?,?,?,?,?,?,?,?,?) }";
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, programaNovios.getCodigoNovios());
			cs.setInt(i++, programaNovios.getNovia().getCodigoEntero());
			cs.setInt(i++, programaNovios.getNovio().getCodigoEntero());
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
			
			codigoNovios = cs.getString(1);
		} catch (SQLException e) {
			codigoNovios = "";
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