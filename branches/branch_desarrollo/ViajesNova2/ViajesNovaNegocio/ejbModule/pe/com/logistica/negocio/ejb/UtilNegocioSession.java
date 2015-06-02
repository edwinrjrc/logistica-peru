package pe.com.logistica.negocio.ejb;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.negocio.dao.MaestroServicioDao;
import pe.com.logistica.negocio.dao.impl.MaestroServicioDaoImpl;

/**
 * Session Bean implementation class UtilNegocioSession
 */
@Stateless(name = "UtilNegocioSession")
public class UtilNegocioSession implements UtilNegocioSessionRemote, UtilNegocioSessionLocal {

	@Override
	public List<DetalleServicioAgencia> agruparServiciosHijos (List<DetalleServicioAgencia> listaServicios){
		List<DetalleServicioAgencia> listaServiciosAgrupados = new ArrayList<DetalleServicioAgencia>();
		
		try {
			for (DetalleServicioAgencia detalleServicioAgencia : listaServicios) {
				detalleServicioAgencia.setServiciosHijos(agruparHijos(detalleServicioAgencia.getServiciosHijos()));
				listaServiciosAgrupados.add(detalleServicioAgencia);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaServiciosAgrupados;
	}
	
	private List<DetalleServicioAgencia> agruparHijos (List<DetalleServicioAgencia> listaServicios){
		List<DetalleServicioAgencia> listaServiciosAgrupados = new ArrayList<DetalleServicioAgencia>();
		
		try {
			MaestroServicioDao maestroServicioDao = new MaestroServicioDaoImpl();
			List<MaestroServicio> listaTiposServicio = null;
			listaTiposServicio = maestroServicioDao.listarMaestroServiciosAdm();
			int agrupados = 0;
			BigDecimal montoAgrupado = null;
			DetalleServicioAgencia detalle = null;
			for (MaestroServicio maestroServicio : listaTiposServicio) {
				detalle = null;
				agrupados = 0;
				montoAgrupado = BigDecimal.ZERO;
				for (DetalleServicioAgencia detalleHijo : listaServicios){
					if (detalleHijo.getTipoServicio().getCodigoEntero().intValue() == maestroServicio.getCodigoEntero().intValue()){
						montoAgrupado = montoAgrupado.add(detalleHijo.getTotalServicio());
						agrupados++;
						/*detalle.setTipoServicio(maestroServicio);
						detalle.setFechaServicio(detalleHijo.getFechaServicio());
						detalle.setCantidad(detalleHijo.getCantidad());
						detalle.setPrecioUnitario(detalleHijo.getPrecioUnitario());
						detalle.setOrigen(detalleHijo.getOrigen());
						detalle.setDestino(detalleHijo.getDestino());*/
						detalle = detalleHijo;
					}
				}
				if (agrupados > 1){
					detalle.setDescripcionServicio("Servicios agrupados :: "+agrupados);
					detalle.setAgrupado(true);
					detalle.setCantidadAgrupados(agrupados);
					detalle.setPrecioUnitario(montoAgrupado); 	
				}
				if (detalle != null){
					listaServiciosAgrupados.add(detalle);
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaServiciosAgrupados;
	}
}
