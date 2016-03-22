/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
* posteriores de la EUPL (la <<Licencia>>);
* Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
* Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
* SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
* ni implicitas.
* Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.validacion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;


import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.validacion.Error;
import es.mitc.redes.urbanismoenred.data.validacion.Proceso;
import es.mitc.redes.urbanismoenred.data.validacion.Resultado;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionInterface.TipoResultado;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless
public class ServicioGeneracionDocumentosBean implements ServicioGeneracionDocumentosLocal, ServicioGeneracionDocumentosRemote {
	
	private class ComparadorResultados implements Comparator<Resultado> {

    	/**
    	 * 
    	 */
		@Override
		public int compare(Resultado r1, Resultado r2) {
			return r1.getValidacion().getNemo().compareTo(r2.getValidacion().getNemo());
		}
    	
    }

	private static final Logger log = Logger.getLogger(ServicioGeneracionDocumentosBean.class);

	private static Color COLORBACKGROUNDCOLUMNA = new Color(0xAB, 0xBC, 0xCD);
	private static Color COLORBACKGROUNDTITULO = new Color(0x8e, 0xb2, 0xd2);
	private static Color COLORBACKGROUNDIMPAR = new Color(222,222,222);
	private static Color COLORBACKGROUNDPAR = new Color(255,255,255);
	
	private static Color COLORFUENTEAVISO = new Color(245,140,0);
	private static Color COLORFUENTEERROR = new Color(255,0,0);
	
	private SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm:ss");

	@EJB
	private ServicioResultadosValidacionLocal gestorResultadosValidacion;
	
	private byte[] crearDocumento(int proceso, TipoValidacion validacion,
			TipoResultado tipoResultado) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		float[] widths = {0.5f, 0.5f};
		PdfPTable table = new PdfPTable(widths);
	
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(0);

		try {

			URL url = getClass().getClassLoader().getResource("imagenes/header_defecto.jpg");
			
			Image header = Image.getInstance(url);
                           
			Document document = new Document(PageSize.A4, 50, 50, 100, 72);
            PdfWriter writer = PdfWriter.getInstance(document, buffer);

            /**Necesario para los manejadores de inicio y fin de cada página*/
            writer.setPageEvent(new PDFGenerator(header));
            document.open();
            document.add(new Phrase("\n"));

            //introduce el contenido del pdf
            List<Resultado> resultados = gestorResultadosValidacion.getResultados(proceso, tipoResultado, validacion);
            
            boolean correcto = true;
            
            for (Resultado resultado : resultados) {
            	if (!resultado.isExito() && resultado.getValidacion().getTipoerror() == 1) {
            		correcto = false;
            		break;
            	}
            }
            
            if (resultados.size() > 0) {
            	insertarInicio(resultados.get(0).getProceso(), table, correcto, widths.length);

                insertarDatosValidacion(resultados, validacion, table, widths.length);

                log.debug("FIN INFORMES");

        	    document.add(table);
            } else {
            	document.add(new Phrase("El proceso está vacío!. Consulte con su administrador.\n"));
            }
            
            
    	   	/** cerramos el pdf ya formado*/
	        document.close();
	        /** cerramos la session que se creo cuando se creo el dao */

		} catch (BadElementException e) {
			log.error("El elemento cargado no es una imagen o esta dañado",e);
		} catch (MalformedURLException e) {
			log.error("La dirección de la cabecera y el pie no es correcta.",e);
		} catch (IOException e) {
			log.error("Error al obtener las imagenes de la url especificada: " + e.getMessage(),e);
		} catch (DocumentException e) {
			log.error("Error creando el documento pdf. Causa: " + e.getMessage(), e);
		} catch (ExcepcionPersistencia e) {
			log.error("Error al obtener los resultados del proceso. Causa: " + e.getMessage(), e);
		} 

		return buffer.toByteArray();
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioGeneracionDocumentosLocal#generarDocumento(int)
	 */
	@Override
	public byte[] generarDocumento(int proceso){

		return generarDocumento(proceso,TipoValidacion.TODAS);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioGeneracionDocumentosLocal#generarDocumento(int, es.mitc.redes.urbanismoenred.servicios.validacion.TipoValidacion)
	 */
	@Override
	public byte[] generarDocumento(int proceso, TipoValidacion validacion){

		log.debug("Creando PDF de errores de todas las validaciones");
		
		return crearDocumento(proceso,validacion,TipoResultado.TODAS);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioGeneracionDocumentosLocal#generarDocumentoErrores(int)
	 */
	@Override
	public byte[] generarDocumentoErrores(int proceso){

		log.debug("Creando PDF de errores de todas las validaciones");
		return generarDocumentoErrores(proceso, TipoValidacion.TODAS);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioGeneracionDocumentosLocal#generarDocumentoErrores(int, es.mitc.redes.urbanismoenred.servicios.validacion.TipoValidacion)
	 */
	@Override
	public byte[] generarDocumentoErrores(int proceso, TipoValidacion validacion){

		return crearDocumento(proceso, validacion, TipoResultado.ERROR);
	}

	/**
	 * 
	 * @param resultados
	 * @param validacion
	 * @param tab
	 * @param columns
	 */
	private void insertarDatosValidacion(List<Resultado> resultados, TipoValidacion validacion, PdfPTable tab, int columns){
		
    	PdfPCell cell = new PdfPCell(new Phrase("Validaciones realizadas"));
    	cell.setBackgroundColor(COLORBACKGROUNDTITULO);
		cell.setColspan(columns);
		cell.setBorder(0);
		//cell.setBorderColor(new Color(255,255,255));
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		tab.addCell(cell);

		String codigoValidacion;
		
		Resultado[] resultadosOrdenados = resultados.toArray(new Resultado[0]);
		
		Arrays.sort(resultadosOrdenados, new ComparadorResultados());
		
    	for(Resultado resultado : resultadosOrdenados){
    		cell = new PdfPCell();
        	cell.setBackgroundColor(COLORBACKGROUNDCOLUMNA);
        	cell.setBorder(0);
        	cell.setColspan(columns);
        	codigoValidacion = resultado.getValidacion().getNemo();
        	cell.setPhrase(new Phrase((codigoValidacion != null ? codigoValidacion+"\t" : "") + resultado.getValidacion().getDescripcion()));
        	tab.addCell(cell);

        	Set<Error> errores;
			if (resultado.isExito()) {
				cell = new PdfPCell(new Phrase("Sin errores"));
				cell.setBorder(0);
				cell.setMinimumHeight(20);
	        	cell.setColspan(columns);
	        	tab.addCell(cell);
        	} else {
        		errores = resultado.getErrors();
        		int i = 0;
        		Phrase frase;
        		Color colorFuente = resultado.getValidacion().getTipoerror() == 1? COLORFUENTEERROR: COLORFUENTEAVISO;
        		for (Error error : errores) {
        			frase = new Phrase(error.getMensaje());
        			frase.getFont().setColor(colorFuente);
        			
        			cell = new PdfPCell(frase);
        			if(i % 2 == 1)  {
        				cell.setBackgroundColor(COLORBACKGROUNDIMPAR);
        			}
        			else {
        				cell.setBackgroundColor(COLORBACKGROUNDPAR);
        			}
        			cell.setBorder(0);
        			cell.setPadding(5);
        			cell.setColspan(columns);
                	tab.addCell(cell);
                	i++;
        		}
        	}
    	
   			cell = new PdfPCell(new Phrase(""));
			cell.setBorder(0);
			cell.setMinimumHeight(20);
        	cell.setColspan(columns);
        	tab.addCell(cell);

    	}

	}

	/**
	 * Introduce el texto que compone el preámbulo del informe.
	 * 
	 * @param proceso
	 * @param table
	 * @param correcto 
	 * @param length
	 */
	private void insertarInicio(Proceso proceso, PdfPTable table, boolean correcto, int length) {
		PdfPCell cell = new PdfPCell(new Phrase("Informe de validación"));
	
    	cell.setBackgroundColor(COLORBACKGROUNDTITULO);
		cell.setColspan(length);
		cell.setBorder(0);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Trámite validado: "));
		cell.setBorder(0);
		cell.setPadding(5);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(proceso.getIdfip()));
		cell.setBorder(0);
		cell.setPadding(5);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Fecha de inicio de la validación: "));
		cell.setBorder(0);
		cell.setPadding(5);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(formateador.format(proceso.getInicio())));
		cell.setBorder(0);
		cell.setPadding(5);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Fecha de finalización de la validación: "));
		cell.setBorder(0);
		cell.setPadding(5);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(formateador.format(proceso.getFin())));
		cell.setBorder(0);
		cell.setPadding(5);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Resultado de la validación: "));
		cell.setBorder(0);
		cell.setPadding(5);
		table.addCell(cell);
		Phrase frase;
		if (correcto) {
			frase = new Phrase("Sin errores.");
		} else {
			frase = new Phrase("Se han detectado errores.");
			frase.getFont().setColor(COLORFUENTEERROR);
		}
		
		cell = new PdfPCell(frase);
		
		cell.setBorder(0);
		cell.setPadding(5);
		table.addCell(cell);
	}
}

