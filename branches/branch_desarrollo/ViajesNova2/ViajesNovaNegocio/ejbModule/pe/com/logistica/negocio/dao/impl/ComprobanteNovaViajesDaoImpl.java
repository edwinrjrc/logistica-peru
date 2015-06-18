/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.ComprobanteBusqueda;
import pe.com.logistica.negocio.dao.ComprobanteNovaViajesDao;
import pe.com.logistica.negocio.util.UtilConexion;
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

	@Override
	public List<Comprobante> consultarComprobantes(
			ComprobanteBusqueda comprobanteBusqueda) throws SQLException {
		List<Comprobante> resultado = null; 
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "{ ? = call negocio.fn_consultarcomprobantesgenerados(?,?,?,?,?,?,?)}";
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			
			if (comprobanteBusqueda.getCodigoEntero() != null){
				cs.setInt(i++, comprobanteBusqueda.getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (comprobanteBusqueda.getIdServicio() != null){
				cs.setInt(i++, comprobanteBusqueda.getIdServicio().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (comprobanteBusqueda.getTitular().getCodigoEntero() != null){
				cs.setInt(i++, comprobanteBusqueda.getTitular().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (comprobanteBusqueda.getTipoComprobante().getCodigoEntero() != null){
				cs.setInt(i++, comprobanteBusqueda.getTipoComprobante().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(comprobanteBusqueda.getNumeroComprobante())){
				cs.setString(i++, comprobanteBusqueda.getNumeroComprobante());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(comprobanteBusqueda.getFechaDesde()));
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(comprobanteBusqueda.getFechaHasta()));
			
			cs.execute();
			rs = (ResultSet) cs.getObject(1);
			
			resultado = new ArrayList<Comprobante>();
			Comprobante comprobante = null;
			while (rs.next()){
				comprobante = new Comprobante();
				comprobante.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				comprobante.getTipoComprobante().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipocomprobante"));
				comprobante.getTipoComprobante().setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				comprobante.setNumeroComprobante(UtilJdbc.obtenerCadena(rs, "numerocomprobante"));
				comprobante.getTitular().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtitular"));
				comprobante.getTitular().setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				comprobante.getTitular().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				comprobante.getTitular().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				comprobante.setFechaComprobante(UtilJdbc.obtenerFecha(rs, "fechacomprobante"));
				comprobante.setTotalComprobante(UtilJdbc.obtenerBigDecimal(rs, "totalcomprobante"));
				resultado.add(comprobante);
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
				throw new SQLException(e);
			}
		}
		return resultado;
	}

}
