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

import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.negocio.dao.ProveedorDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 *
 */
public class ProveedorDaoImpl implements ProveedorDao {

	/**
	 * 
	 */
	public ProveedorDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ProveedorDao#registroProveedor(pe.com.logistica.bean.negocio.Proveedor)
	 */
	@Override
	public void registroProveedor(Proveedor proveedor, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarpersonaproveedor(?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, proveedor.getCodigoEntero());
			cs.setInt(i++, proveedor.getRubro().getCodigoEntero());
			
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
	public List<Proveedor> listarProveedor(Proveedor proveedor)
			throws SQLException {
		List<Proveedor> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "select * " +
				" from negocio.vw_proveedor";

		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			rs = cs.executeQuery();
			
			resultado = new ArrayList<Proveedor>();
			Proveedor proveedor2 = null;
			while (rs.next()){
				proveedor2 = new Proveedor();
				proveedor2.getDocumentoIdentidad().getTipoDocumento().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				proveedor2.getDocumentoIdentidad().getTipoDocumento().setNombre(UtilJdbc.obtenerCadena(rs, "nombretipodocumento"));
				proveedor2.getDocumentoIdentidad().setNumeroDocumento(UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				proveedor2.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				proveedor2.setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno"));
				proveedor2.setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno"));
				proveedor2.getRubro().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idrubro"));
				proveedor2.getRubro().setNombre(UtilJdbc.obtenerCadena(rs, "nombrerubro"));
				proveedor2.getDireccion().getVia().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idvia"));
				proveedor2.getDireccion().getVia().setNombre(UtilJdbc.obtenerCadena(rs, "nombretipovia"));
				proveedor2.getDireccion().setNombreVia(UtilJdbc.obtenerCadena(rs, "nombrevia"));
				proveedor2.getDireccion().setNumero(UtilJdbc.obtenerCadena(rs, "numero"));
				proveedor2.getDireccion().setInterior(UtilJdbc.obtenerCadena(rs, "interior"));
				proveedor2.getDireccion().setManzana(UtilJdbc.obtenerCadena(rs, "manzana"));
				proveedor2.getDireccion().setLote(UtilJdbc.obtenerCadena(rs, "lote"));
				Telefono teldireccion = new Telefono();
				teldireccion.setNumeroTelefono(UtilJdbc.obtenerCadena(rs, "teledireccion"));
				proveedor2.getDireccion().getTelefonos().add(teldireccion);
				resultado.add(proveedor2);
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
}
