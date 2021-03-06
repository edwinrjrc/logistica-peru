/**
 * 
 */
package pe.com.logistica.web.faces;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import pe.com.logistica.bean.cargaexcel.CeldaExcel;
import pe.com.logistica.bean.cargaexcel.ColumnasExcel;
import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
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
	private ReporteArchivo reporteArchivo;

	private List<CeldaExcel> tablaExcelCargada;
	private ColumnasExcel columnasExcel;
	private InputStream streamArchivo;

	private List<ColumnasExcel> dataExcel = null;
	
	private boolean tablaLlena;

	public CargaReporteProveedorMBean() {
		// TODO Auto-generated constructor stub
	}

	public void listenerExcel(FileUploadEvent event) {
		UploadedFile archivo = event.getUploadedFile();
		try {
			this.setTablaLlena(false);
			this.setDataExcel(null);
			this.setStreamArchivo(archivo.getInputStream());
			
			this.setTablaLlena(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cargarArchivoExcel() {
		HSSFWorkbook archivoExcel = null;
		try {
			if (this.getStreamArchivo() != null){
				this.setDataExcel(null);
				archivoExcel = new HSSFWorkbook(this.getStreamArchivo());
				HSSFSheet hojaInicial = archivoExcel.getSheetAt(0);
				//int ultimaColumna = hojaInicial.getLastRowNum();
				//Iterator<Row> filas = hojaInicial.rowIterator();
				HSSFRow fila = null;
				HSSFCell celda = null;
				int iCelda = 0;
				List<String> cabecera = new ArrayList<String>();
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
				
				boolean registroCabecera = false;
				CeldaExcel celdaExcel = null;
				ColumnasExcel columna = null;
				Method method = null;
				Method method2 = null;
				Method method3 = null;
				String metodo = "getColumna";
				String metodo2 = "setValorCadena";
				String metodo3 = "setMostrar";
				Object ob1 = null;
				for (int i=(this.getFilaInicial()-1); i<hojaInicial.getLastRowNum(); i++){
					fila = hojaInicial.getRow(i);
					iCelda = this.getColumnaInicial();
					celda = null;
					while (!registroCabecera && iCelda < (this.getNroColumnas()-1)){
						celda = fila.getCell(iCelda);
						String dato = UtilWeb.obtenerDato(celda);
						cabecera.add(dato);
						iCelda++;
					}
					if (cabecera.size()>0){
						columna = new ColumnasExcel();
						registroCabecera = true;
					}
					if (i > this.getFilaInicial()){
						int j=1;
						while (iCelda < this.getNroColumnas()){
							celda = fila.getCell(iCelda);
							String dato = UtilWeb.obtenerDato(celda);
							
							method = columna.getClass().getMethod(metodo+(j), null);
							ob1 = method.invoke(columna, null);
							method2 = ob1.getClass().getMethod(metodo2, String.class);
							Object ob2 = method2.invoke(ob1, dato);
							method3 = ob1.getClass().getMethod(metodo3, boolean.class);
							method3.invoke(ob1, true);
							iCelda++;
							j++;
						}
						this.getDataExcel().add(columna);
					}
				}
				
				method = null;
				method2 = null;
				metodo = "getColumna";
				metodo2 = "setNombreColumna";
				ob1 = null;
				for (int i=0; i<cabecera.size(); i++) {
					if (StringUtils.isNotBlank(cabecera.get(i))){
						method = this.getColumnasExcel().getClass().getMethod(metodo+(i+1), null);
						ob1 = method.invoke(this.getColumnasExcel(), null);
						method2 = ob1.getClass().getMethod(metodo2, String.class);
						method2.invoke(ob1, cabecera.get(i));
						method3 = ob1.getClass().getMethod(metodo3, boolean.class);
						method3.invoke(ob1, true);
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
	 * @return the dataExcel
	 */
	public List<ColumnasExcel> getDataExcel() {
		if (dataExcel == null){
			dataExcel = new ArrayList<ColumnasExcel>();
		}
		return dataExcel;
	}

	/**
	 * @param dataExcel the dataExcel to set
	 */
	public void setDataExcel(List<ColumnasExcel> dataExcel) {
		this.dataExcel = dataExcel;
	}

	/**
	 * @return the tablaLlena
	 */
	public boolean isTablaLlena() {
		return tablaLlena;
	}

	/**
	 * @param tablaLlena the tablaLlena to set
	 */
	public void setTablaLlena(boolean tablaLlena) {
		this.tablaLlena = tablaLlena;
	}

	/**
	 * @return the reporteArchivo
	 */
	public ReporteArchivo getReporteArchivo() {
		if (reporteArchivo == null){
			reporteArchivo = new ReporteArchivo();
		}
		return reporteArchivo;
	}

	/**
	 * @param reporteArchivo the reporteArchivo to set
	 */
	public void setReporteArchivo(ReporteArchivo reporteArchivo) {
		this.reporteArchivo = reporteArchivo;
	}
}
