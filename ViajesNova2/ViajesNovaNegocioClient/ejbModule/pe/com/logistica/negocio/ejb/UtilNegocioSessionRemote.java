package pe.com.logistica.negocio.ejb;

import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;

@Remote
public interface UtilNegocioSessionRemote {

	List<DetalleServicioAgencia> agruparServiciosHijos(
			List<DetalleServicioAgencia> listaServicios);

}
