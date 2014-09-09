/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.io.InputStream;

/**
 * @author Edwin
 *
 */
public class ArchivoAdjunto {
	
	private InputStream stream;
	private String nombreArchivo;
	private String tipoContenido;
	
	
	public ArchivoAdjunto() {
		// TODO Auto-generated constructor stub
	}
	
	public ArchivoAdjunto(String nombre) {
		this.nombreArchivo = nombre;
	}
	/**
	 * @return the stream
	 */
	public InputStream getStream() {
		return stream;
	}

	/**
	 * @param stream the stream to set
	 */
	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	/**
	 * @return the nombreArchivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	/**
	 * @return the tipoContenido
	 */
	public String getTipoContenido() {
		return tipoContenido;
	}

	/**
	 * @param tipoContenido the tipoContenido to set
	 */
	public void setTipoContenido(String tipoContenido) {
		this.tipoContenido = tipoContenido;
	}

}
