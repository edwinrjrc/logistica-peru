/**
 * 
 */
package pe.com.logistica.web.faces;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import pe.com.logistica.bean.cargaexcel.CeldaExcel;
import pe.com.logistica.bean.cargaexcel.ColumnasExcel;
import pe.com.logistica.web.util.UtilWeb;

/**
 * @author EDWREB
 *
 */
@ManagedBean(name = "cargaReporteProveedorMBean")
@SessionScoped()
public class CargaReporteProveedorMBean extends BaseMBean {

	private final static Logger logger = Logger.getLogger(CargaReporteProveedorMBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 6607933550231690113L;

	private Integer filaInicial;
	private Integer columnaInicial;
	private Integer nroColumnas;

	private List<CeldaExcel> tablaExcelCargada;
	private ColumnasExcel columnasExcel;
	private InputStream streamArchivo;
	
	private byte[] datosExcel;

	public CargaReporteProveedorMBean() {
		// TODO Auto-generated constructor stub
	}

	public void listenerExcel(FileUploadEvent event) {
		UploadedFile archivo = event.getUploadedFile();
		try {
			this.setStreamArchivo(archivo.getInputStream());
			this.setDatosExcel(archivo.getData());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cargarArchivoExcel() {
		HSSFWorkbook archivoExcel = null;
		try {
			if (this.getStreamArchivo() != null){
				archivoExcel = new HSSFWorkbook(this.getStreamArchivo());
				HSSFSheet hojaInicial = archivoExcel.getSheetAt(0);
				int ultimaColumna = hojaInicial.getLastRowNum();
				System.out.println("ultima ::"+ultimaColumna);
				//Iterator<Row> filas = hojaInicial.rowIterator();
				HSSFRow fila = null;
				HSSFCell celda = null;
				int iCelda = 0;
				List<String> cabecera = new ArrayList<String>();
				List<ColumnasExcel> dataExcel = new ArrayList<ColumnasExcel>();
				/*while (filas.hasNext()){
					fila = (HSSFRow) filas.next();
					iCelda = 0;
					celda = null;
					while (iCelda < this.getNroColumnas()){
						celda = fila.getCell(iCelda);
						cabecera.add(UtilWeb.obtenerDato(celda));
						iCelda++;
					}
					break;
				}*/
				
				System.out.println("1. Cabecera ::"+cabecera.size());
				boolean registroCabecera = false;
				CeldaExcel celdaExcel = null;
				for (int i=this.getFilaInicial(); i<hojaInicial.getLastRowNum(); i++){
					fila = hojaInicial.getRow(i);
					iCelda = this.getColumnaInicial();
					celda = null;
					while (!registroCabecera && iCelda < this.getNroColumnas()){
						celda = fila.getCell(iCelda);
						String dato = UtilWeb.obtenerDato(celda);
						System.out.println("Celda ::"+iCelda+", valor::"+dato);
						cabecera.add(dato);
						iCelda++;
					}
					registroCabecera = (cabecera.size()>0);
					while (iCelda < this.getNroColumnas()){
						celda = fila.getCell(iCelda);
						String dato = UtilWeb.obtenerDato(celda);
						System.out.println("Celda ::"+iCelda+", valor::"+dato);
						cabecera.add(dato);
						iCelda++;
					}
				}
				
				System.out.println("2. Cabecera ::"+cabecera.size());
				/*for (String string : cabecera) {
					System.out.println("::"+string);
				}*/
				Method method = null;
				Method method2 = null;
				String metodo = "getColumna";
				String metodo2 = "setNombreColumna";
				Object ob1 = null;
				for (int i=0; i<cabecera.size(); i++) {
					if (StringUtils.isNotBlank(cabecera.get(i))){
						method = this.getColumnasExcel().getClass().getMethod(metodo+(i+1), null);
						ob1 = method.invoke(this.getColumnasExcel(), null);
						method2 = ob1.getClass().getMethod(metodo2, String.class);
						method2.invoke(ob1, cabecera.get(i));
					}
				}
			}

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
		} finally{
			try {
				if (this.getStreamArchivo() != null){
					this.getStreamArchivo().reset();
					this.getStreamArchivo().close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * ========================================================================
	 * ===========
	 */

	/**
	 * @return the filaInicial
	 */
	public Integer getFilaInicial() {
		if (filaInicial == null) {
			filaInicial = 1;
		}
		return filaInicial;
	}

	/**
	 * @param filaInicial
	 *            the filaInicial to set
	 */
	public void setFilaInicial(Integer filaInicial) {
		this.filaInicial = filaInicial;
	}

	/**
	 * @return the columnaInicial
	 */
	public Integer getColumnaInicial() {
		if (columnaInicial == null) {
			columnaInicial = 1;
		}
		return columnaInicial;
	}

	/**
	 * @param columnaInicial
	 *            the columnaInicial to set
	 */
	public void setColumnaInicial(Integer columnaInicial) {
		this.columnaInicial = columnaInicial;
	}

	/**
	 * @return the nroColumnas
	 */
	public Integer getNroColumnas() {
		return nroColumnas;
	}

	/**
	 * @param nroColumnas
	 *            the nroColumnas to set
	 */
	public void setNroColumnas(Integer nroColumnas) {
		this.nroColumnas = nroColumnas;
	}

	/**
	 * @return the tablaExcelCargada
	 */
	public List<CeldaExcel> getTablaExcelCargada() {
		if (tablaExcelCargada == null) {
			tablaExcelCargada = new ArrayList<CeldaExcel>();
		}
		return tablaExcelCargada;
	}

	/**
	 * @param tablaExcelCargada
	 *            the tablaExcelCargada to set
	 */
	public void setTablaExcelCargada(List<CeldaExcel> tablaExcelCargada) {
		this.tablaExcelCargada = tablaExcelCargada;
	}

	/**
	 * @return the columnasExcel
	 */
	public ColumnasExcel getColumnasExcel() {
		if (columnasExcel == null) {
			columnasExcel = new ColumnasExcel();
		}
		return columnasExcel;
	}

	/**
	 * @param columnasExcel
	 *            the columnasExcel to set
	 */
	public void setColumnasExcel(ColumnasExcel columnasExcel) {
		this.columnasExcel = columnasExcel;
	}

	/**
	 * @return the streamArchivo
	 */
	public InputStream getStreamArchivo() {
		return streamArchivo;
	}

	/**
	 * @param streamArchivo the streamArchivo to set
	 */
	public void setStreamArchivo(InputStream streamArchivo) {
		this.streamArchivo = streamArchivo;
	}

	/**
	 * @return the datosExcel
	 */
	public byte[] getDatosExcel() {
		return datosExcel;
	}

	/**
	 * @param datosExcel the datosExcel to set
	 */
	public void setDatosExcel(byte[] datosExcel) {
		this.datosExcel = datosExcel;
	}
}
