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

import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.negocio.dao.MaestroServicioDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author edwreb
 *
 */
public class MaestroServicioDaoImpl implements MaestroServicioDao {

	/**
	 * 
	 */
	public MaestroServicioDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroServicioDao#listarMaestroServicios()
	 */
	@Override
	public List<MaestroServicio> listarMaestroServicios() throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_listarmaestroservicios() }";
		ResultSet rs = null;
		List<MaestroServicio> resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			resultado = new ArrayList<MaestroServicio>();
			MaestroServicio bean = null;
			while (rs.next()) {
				bean = new MaestroServicio();
				bean.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				bean.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				bean.setDescripcion(UtilJdbc.obtenerCadena(rs, "descripcion"));
				bean.setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				bean.setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				bean.setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				bean.setUsuarioCreacion(UtilJdbc.obtenerCadena(rs, "usuariocreacion"));
				bean.setFechaCreacion(UtilJdbc.obtenerFecha(rs, "fechacreacion"));
				bean.setIpCreacion(UtilJdbc.obtenerCadena(rs, "ipcreacion"));
				bean.setUsuarioModificacion(UtilJdbc.obtenerCadena(rs, "usuariomodificacion"));
				bean.setFechaModificacion(UtilJdbc.obtenerFecha(rs, "fechamodificacion"));
				bean.setIpModificacion(UtilJdbc.obtenerCadena(rs, "ipmodificacion"));
				resultado.add(bean);
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

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroServicioDao#ingresarMaestroServicio(pe.com.logistica.bean.negocio.MaestroServicio)
	 */
	@Override
	public Integer ingresarMaestroServicio(MaestroServicio servicio)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarservicio(?,?,?,?,?,?,?,?,?,?,?,?) }";
		Integer resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getNombre()));
			if (StringUtils.isNotBlank(servicio.getDescripcionCorta())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getDescripcionCorta()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(servicio.getDescripcion())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getDescripcion()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setBoolean(i++, servicio.isRequiereFee());
			if (servicio.getServicioFee().getCodigoEntero()!= null && servicio.getServicioFee().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicio.getServicioFee().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			
			cs.setBoolean(i++, servicio.isPagaImpto());
			if (servicio.getServicioImpto().getCodigoEntero()!= null && servicio.getServicioImpto().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicio.getServicioImpto().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setBoolean(i++, servicio.isCargaComision());
			cs.setBoolean(i++, servicio.isCargaIgv());
			if (servicio.getValorComision() != null){
				cs.setBigDecimal(i++, servicio.getValorComision());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			cs.setString(i++, servicio.getUsuarioCreacion());
			cs.setString(i++, servicio.getIpCreacion());
			cs.execute();

			resultado = cs.getInt(1);
		} catch (SQLException e) {
			resultado = null;
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

		return resultado;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroServicioDao#actualizarMaestroServicio(pe.com.logistica.bean.negocio.MaestroServicio)
	 */
	@Override
	public boolean actualizarMaestroServicio(MaestroServicio servicio)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarservicio(?,?,?,?,?,?,?,?,?,?) }";
		boolean resultado = false;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicio.getCodigoEntero().intValue());
			cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getNombre()));
			if (StringUtils.isNotBlank(servicio.getDescripcion())){
				cs.setString(i++, UtilJdbc.convertirMayuscula(servicio.getDescripcion()));
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setBoolean(i++, servicio.isRequiereFee());
			cs.setBoolean(i++, servicio.isPagaImpto());
			cs.setBoolean(i++, servicio.isCargaComision());
			cs.setBoolean(i++, servicio.isEsImpuesto());
			cs.setBoolean(i++, servicio.isEsFee());
			cs.setString(i++, servicio.getUsuarioCreacion());
			cs.setString(i++, servicio.getIpCreacion());
			cs.execute();

			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
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

		return resultado;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroServicioDao#consultarMaestroServicio(int)
	 */
	@Override
	public MaestroServicio consultarMaestroServicio(int idMaestroServicio)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_consultarservicio(?) }";
		ResultSet rs = null;
		MaestroServicio resultado = null;

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, idMaestroServicio);
			cs.execute();

			rs = (ResultSet) cs.getObject(1);
			if (rs.next()) {
				resultado = new MaestroServicio();
				resultado.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				resultado.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				resultado.setDescripcion(UtilJdbc.obtenerCadena(rs, "descripcion"));
				resultado.setRequiereFee(UtilJdbc.obtenerBoolean(rs, "requierefee"));
				resultado.setPagaImpto(UtilJdbc.obtenerBoolean(rs, "pagaimpto"));
				resultado.setCargaComision(UtilJdbc.obtenerBoolean(rs, "cargacomision"));
				resultado.setEsImpuesto(UtilJdbc.obtenerBoolean(rs, "esimpuesto"));
				resultado.setEsFee(UtilJdbc.obtenerBoolean(rs, "esfee"));
				resultado.setUsuarioCreacion(UtilJdbc.obtenerCadena(rs, "usuariocreacion"));
				resultado.setFechaCreacion(UtilJdbc.obtenerFecha(rs, "fechacreacion"));
				resultado.setIpCreacion(UtilJdbc.obtenerCadena(rs, "ipcreacion"));
				resultado.setUsuarioModificacion(UtilJdbc.obtenerCadena(rs, "usuariomodificacion"));
				resultado.setFechaModificacion(UtilJdbc.obtenerFecha(rs, "fechamodificacion"));
				resultado.setIpModificacion(UtilJdbc.obtenerCadena(rs, "ipmodificacion"));
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
					throw new SQLException(e1);
				}
			}
		}

		return resultado;
	}

}
