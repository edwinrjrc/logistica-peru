/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.util.Date;
import java.util.List;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author edwreb
 *
 */
public class ServicioAgencia extends BaseNegocio{
	/**
	 * 
	 */
	private static final long serialVersionUID = 52998145587384961L;
	
	private Cliente cliente;
	private Date fechaServicio;
	private int cantidadServicios;
	private BaseVO destino;
	
	private List<DetalleServicioAgencia> listaDetalleServicio;
	

	/**
	 * 
	 */
	public ServicioAgencia() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}



	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}



	/**
	 * @return the fechaServicio
	 */
	public Date getFechaServicio() {
		return fechaServicio;
	}



	/**
	 * @param fechaServicio the fechaServicio to set
	 */
	public void setFechaServicio(Date fechaServicio) {
		this.fechaServicio = fechaServicio;
	}



	/**
	 * @return the cantidadServicios
	 */
	public int getCantidadServicios() {
		return cantidadServicios;
	}



	/**
	 * @param cantidadServicios the cantidadServicios to set
	 */
	public void setCantidadServicios(int cantidadServicios) {
		this.cantidadServicios = cantidadServicios;
	}



	/**
	 * @return the destino
	 */
	public BaseVO getDestino() {
		return destino;
	}



	/**
	 * @param destino the destino to set
	 */
	public void setDestino(BaseVO destino) {
		this.destino = destino;
	}



	/**
	 * @return the listaDetalleServicio
	 */
	public List<DetalleServicioAgencia> getListaDetalleServicio() {
		return listaDetalleServicio;
	}



	/**
	 * @param listaDetalleServicio the listaDetalleServicio to set
	 */
	public void setListaDetalleServicio(List<DetalleServicioAgencia> listaDetalleServicio) {
		this.listaDetalleServicio = listaDetalleServicio;
	}

}
