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

import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class MaestroDaoImpl implements MaestroDao {

	/**
	 * 
	 */
	public MaestroDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroDao#listarMaestros()
	 */
	@Override
	public List<Maestro> listarMaestros() throws SQLException {
		List<Maestro> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_listamaestros";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Maestro>();
			Maestro maestro = null;
			while(rs.next()){
				maestro = new Maestro();
				maestro.setCodigoEntero(rs.getInt("id"));
				maestro.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				maestro.setDescripcion(UtilJdbc.obtenerCadena(rs,"descripcion"));
				maestro.getEstado().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "estado"));
				maestro.getEstado().setNombre(UtilJdbc.obtenerCadena(rs, "descestado"));
				resultado.add(maestro);
			}
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
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
	
	@Override
	public Maestro consultarMaestro(int id) throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_listamaestros where id = ?";
		Maestro maestro = null;
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, id);
			rs = cs.executeQuery();
			
			
			if(rs.next()){
				maestro = new Maestro();
				maestro.setCodigoEntero(rs.getInt("id"));
				maestro.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				maestro.setDescripcion(UtilJdbc.obtenerCadena(rs,"descripcion"));
				maestro.getEstado().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "estado"));
				maestro.getEstado().setNombre(UtilJdbc.obtenerCadena(rs, "descestado"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
		
		return maestro;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroDao#listarHijosMaestro(int)
	 */
	@Override
	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException {
		List<Maestro> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from soporte.vw_listahijosmaestro where idmaestro = ?";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, idmaestro);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Maestro>();
			Maestro maestro = null;
			while(rs.next()){
				maestro = new Maestro();
				maestro.setCodigoEntero(rs.getInt("id"));
				maestro.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				maestro.setDescripcion(UtilJdbc.obtenerCadena(rs,"descripcion"));
				maestro.getEstado().setCodigoCadena(UtilJdbc.obtenerCadena(rs, "estado"));
				maestro.getEstado().setNombre(UtilJdbc.obtenerCadena(rs, "descestado"));
				resultado.add(maestro);
			}
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
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
	 * @see pe.com.logistica.negocio.dao.MaestroDao#ingresarMaestro(pe.com.logistica.bean.negocio.Maestro)
	 */
	@Override
	public void ingresarMaestro(Maestro maestro) throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_ingresarmaestro(?,?) }";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, maestro.getNombre());
			cs.setString(i++, maestro.getDescripcion());
			
			cs.execute();
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.MaestroDao#ingresarHijoMaestro(pe.com.logistica.bean.negocio.Maestro)
	 */
	@Override
	public void ingresarHijoMaestro(Maestro maestro) throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_ingresarhijomaestro(?,?,?) }";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, maestro.getCodigoMaestro());
			cs.setString(i++, maestro.getNombre());
			cs.setString(i++, maestro.getDescripcion());
			
			cs.execute();
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null){
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}
	}
}