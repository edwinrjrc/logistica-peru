/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.negocio.dao.DireccionDao;

/**
 * @author Edwin
 *
 */
public class DireccionDaoImpl implements DireccionDao {

	/**
	 * 
	 */
	public DireccionDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void registrarDireccion(Direccion direccion, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresardireccion(?,?,?,?,?,?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, direccion.getVia().getCodigoEntero());
			cs.setString(i++, direccion.getNombreVia());
			cs.setString(i++, direccion.getNumero());
			cs.setString(i++, direccion.getInterior());
			cs.setString(i++, direccion.getManzana());
			cs.setString(i++, direccion.getLote());
			cs.setString(i++, direccion.getUbigeo().getCodigoCadena());
			cs.setString(i++, direccion.getUsuarioCreacion());
			cs.setString(i++, direccion.getIpCreacion());
			
			
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

}
