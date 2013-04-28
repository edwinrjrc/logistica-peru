/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.negocio.dao.CatalogoDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class CatalogoDaoImpl implements CatalogoDao {

	/**
	 * 
	 */
	public CatalogoDaoImpl() {
	}

	@Override
	public List<BaseVO> listarRoles() {
		List<BaseVO> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select id, nombre from seguridad.rol";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<BaseVO>();
			BaseVO rol = null;
			while(rs.next()){
				rol = new BaseVO();
				rol.setCodigoEntero(rs.getInt("id"));
				rol.setNombre(UtilJdbc.obtenerCadena(rs, "nombre"));
				resultado.add(rol);
			}
		} catch (SQLException e) {
			resultado = null;
			e.printStackTrace();
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
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		return resultado;
	}

}
