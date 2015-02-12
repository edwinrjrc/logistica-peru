/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import pe.com.logistica.bean.negocio.ConfiguracionTipoServicio;
import pe.com.logistica.negocio.dao.ConfiguracionServicioDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class ConfiguracionServicioDaoImpl implements ConfiguracionServicioDao {

	/**
	 * 
	 */
	public ConfiguracionServicioDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ConfiguracionServicioDao#consultarConfiguracionServicio()
	 */
	@Override
	public ConfiguracionTipoServicio consultarConfiguracionServicio(Integer idTipoServicio)
			throws SQLException, Exception {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_consultarconfiguracionservicio(?) }";
		ResultSet rs = null;
		ConfiguracionTipoServicio resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idTipoServicio.intValue());
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			
			if (rs.next()) {
				resultado = new ConfiguracionTipoServicio();
				resultado.setMuestraAerolinea(UtilJdbc.obtenerBoolean(rs, "muestraaerolinea"));
				resultado.setMuestraEmpresaTransporte(UtilJdbc.obtenerBoolean(rs, "muestraempresatransporte"));
				resultado.setMuestraHotel(UtilJdbc.obtenerBoolean(rs, "muestrahotel"));
				resultado.setMuestraProveedor(UtilJdbc.obtenerBoolean(rs, "muestraproveedor"));	
				resultado.setMuestraDescServicio(UtilJdbc.obtenerBoolean(rs, "muestradescservicio"));
				resultado.setMuestraFechaServicio(UtilJdbc.obtenerBoolean(rs, "muestrafechaservicio"));
				resultado.setMuestraFechaRegreso(UtilJdbc.obtenerBoolean(rs, "muestrafecharegreso"));
				resultado.setMuestraCantidad(UtilJdbc.obtenerBoolean(rs, "muestracantidad"));
				resultado.setMuestraPrecioBase(UtilJdbc.obtenerBoolean(rs, "muestraprecio"));
				resultado.setMuestraDestino(UtilJdbc.obtenerBoolean(rs, "muestradestino"));
				resultado.setMuestraComision(UtilJdbc.obtenerBoolean(rs, "muestracomision"));
				resultado.setMuestraOperadora(UtilJdbc.obtenerBoolean(rs, "muestraoperador"));
				resultado.setMuestraTarifaNegociada(UtilJdbc.obtenerBoolean(rs, "muestratarifanegociada"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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

		return resultado;
	}

}
