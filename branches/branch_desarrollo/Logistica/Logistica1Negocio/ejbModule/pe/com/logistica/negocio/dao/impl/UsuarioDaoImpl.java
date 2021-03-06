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

import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.dao.UsuarioDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class UsuarioDaoImpl implements UsuarioDao{

	/**
	 * 
	 */
	public UsuarioDaoImpl() {
	}

	@Override
	public boolean registrarUsuario(Usuario usuario) throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call seguridad.fn_ingresarusuario(?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getUsuario()));
			cs.setString(i++, usuario.getCredencial());
			cs.setInt(i++, usuario.getRol().getCodigoEntero());
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getNombres()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getApellidoPaterno()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getApellidoMaterno()));
			boolean resul = cs.execute();
			
			System.out.println("resultado execute ::"+resul);
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
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
	public boolean actualizarUsuario(Usuario usuario) throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call seguridad.fn_actualizarusuario(?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, usuario.getCodigoEntero());
			cs.setInt(i++, usuario.getRol().getCodigoEntero());
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getNombres()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getApellidoPaterno()));
			cs.setString(i++, UtilJdbc.convertirMayuscula(usuario.getApellidoMaterno()));
			
			boolean resul = cs.execute();
			
			System.out.println("resultado execute ::"+resul);
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
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
	public List<Usuario> listarUsuarios() throws SQLException {
		List<Usuario> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * from seguridad.vw_listarusuarios";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Usuario>();
			Usuario usuario = null;
			while(rs.next()){
				usuario = new Usuario();
				usuario.setCodigoEntero(rs.getInt("id"));
				usuario.setUsuario(UtilJdbc.obtenerCadena(rs, "usuario"));
				usuario.getRol().setCodigoEntero(rs.getInt("id_rol"));
				usuario.getRol().setNombre(UtilJdbc.obtenerCadena(rs,"nombre"));
				usuario.setNombres(UtilJdbc.obtenerCadena(rs,"nombres"));
				usuario.setApellidoPaterno(UtilJdbc.obtenerCadena(rs,"apepaterno"));
				usuario.setApellidoMaterno(UtilJdbc.obtenerCadena(rs,"apematerno"));
				resultado.add(usuario);
			}
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally{
			try {
				if (rs != null){
					rs.close();
				}
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
	public Usuario consultarUsuario(int id) throws SQLException {
		Usuario resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select id, usuario, credencial, id_rol, nombre, nombres, apepaterno, apematerno" +
				" from seguridad.vw_listarusuarios where id = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, id);
			rs = cs.executeQuery();
			
			resultado = new Usuario();
			if (rs.next()){
				resultado.setCodigoEntero(rs.getInt("id"));
				resultado.setUsuario(UtilJdbc.obtenerCadena(rs, "usuario"));
				resultado.setCredencial(UtilJdbc.obtenerCadena(rs, "credencial"));
				resultado.getRol().setCodigoEntero(rs.getInt("id_rol"));
				resultado.getRol().setNombre(UtilJdbc.obtenerCadena(rs,"nombre"));
				resultado.setNombres(UtilJdbc.obtenerCadena(rs,"nombres"));
				resultado.setApellidoPaterno(UtilJdbc.obtenerCadena(rs,"apepaterno"));
				resultado.setApellidoMaterno(UtilJdbc.obtenerCadena(rs,"apematerno"));
			}
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally{
			try {
				if (rs != null){
					rs.close();
				}
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
	public boolean inicioSesion(Usuario usuario) throws SQLException {
		boolean resultado = false;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{? = call seguridad.fn_iniciosesion(?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, usuario.getUsuario());
			cs.setString(i++, usuario.getCredencial());
			
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
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
	public Usuario inicioSesion2(Usuario usuario) throws SQLException {
		Usuario resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select id, usuario, id_rol, nombre, nombres, apepaterno, apematerno" +
				" from seguridad.vw_listarusuarios where usuario = ? and credencial = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setString(1, usuario.getUsuario());
			cs.setString(2, usuario.getCredencial());
			rs = cs.executeQuery();
			
			resultado = new Usuario();
			if (rs.next()){
				resultado.setCodigoEntero(rs.getInt("id"));
				resultado.setUsuario(UtilJdbc.obtenerCadena(rs, "usuario"));
				resultado.getRol().setCodigoEntero(rs.getInt("id_rol"));
				resultado.getRol().setNombre(UtilJdbc.obtenerCadena(rs,"nombre"));
				resultado.setNombres(UtilJdbc.obtenerCadena(rs,"nombres"));
				resultado.setApellidoPaterno(UtilJdbc.obtenerCadena(rs,"apepaterno"));
				resultado.setApellidoMaterno(UtilJdbc.obtenerCadena(rs,"apematerno"));
				resultado.setEncontrado(true);
			}
		} catch (SQLException e) {
			resultado = null;
			throw new SQLException(e);
		} finally{
			try {
				if (rs != null){
					rs.close();
				}
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
}
