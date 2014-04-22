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

import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.negocio.dao.ContactoDao;
import pe.com.logistica.negocio.dao.TelefonoDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class ContactoDaoImpl implements ContactoDao {

	/**
	 * 
	 */
	public ContactoDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ContactoDao#registrarContactoProveedor(pe.com.logistica.bean.negocio.Contacto)
	 */
	@Override
	public void registrarContactoProveedor(int idproveedor, Contacto contacto, Connection conexion)
			throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarcontactoproveedor(?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			if (idproveedor != 0){
				cs.setInt(i++, idproveedor);
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (contacto.getCodigoEntero() != null){
				cs.setInt(i++, contacto.getCodigoEntero());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (contacto.getArea().getCodigoEntero() != null){
				cs.setInt(i++, contacto.getArea().getCodigoEntero());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(contacto.getAnexo())){
				cs.setString(i++, contacto.getAnexo());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			
			cs.execute();
			
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
	}

	@Override
	public List<Contacto> consultarContactoOProveedor(int idproveedor) throws SQLException {
		List<Contacto> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select *" +
				" from negocio.vw_contactoproveedor where idproveedor = ?";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			cs.setInt(1, idproveedor);
			
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Contacto>();
			Contacto contacto = null;
			TelefonoDao telefonoDao = new TelefonoDaoImpl();
			while (rs.next()){
				contacto = new Contacto();
				contacto.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				contacto.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				contacto.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				contacto.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				contacto.getGenero().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idgenero"));
				contacto.getEstadoCivil().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadocivil"));
				contacto.getDocumentoIdentidad().getTipoDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				contacto.getDocumentoIdentidad().setNumeroDocumento(UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				contacto.setUsuarioCreacion(UtilJdbc.obtenerCadena(rs, "usuariocreacion"));
				contacto.setFechaCreacion(UtilJdbc.obtenerFecha(rs, "fechacreacion"));
				contacto.setIpCreacion(UtilJdbc.obtenerCadena(rs, "ipcreacion"));
				contacto.getArea().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idarea"));
				contacto.getArea().setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				contacto.setAnexo(UtilJdbc.obtenerCadena(rs, "anexo"));
				contacto.setListaTelefonos(telefonoDao.consultarTelefonoContacto(UtilJdbc.obtenerNumero(contacto.getCodigoEntero()), conn));
				resultado.add(contacto);
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
