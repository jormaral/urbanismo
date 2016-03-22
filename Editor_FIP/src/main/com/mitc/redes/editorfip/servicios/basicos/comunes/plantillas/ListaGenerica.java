/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
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


package com.mitc.redes.editorfip.servicios.basicos.comunes.plantillas;

import java.util.List;

import javax.ejb.Local;
import javax.faces.model.SelectItem;

@Local
public interface ListaGenerica {
	
	// -----------------  Metodos genericos ------------------------- 
	
	/**
	 * Metodo que inicializa la lista segun la pagina y el maximo
	 * de resultados.
	 * 
	 * Por lo general se debe de hacer la llamada en el page.xml
	 * agregando la etiqueta <action execute="#{miLista.inicalizarLista(miTipo)}" on-postback="false" />
	 * 
	 * @param tipo String que indentifica el 'Entity' sobre 
	 *        cual trabajara la lista. Ej: "Entidad" o "Usuario"
	 */
	public void inicializarLista(String tipo) throws Exception;
	
	public void buscar() throws Exception;
	
	/** 
	 * Ordena la lista por una columna concreta
	 * 
	 * @param columna Nombre de la columna por la cual se va a ordenar
	 * @param sentido Sentido de ordenacion. Puede ser "ASC" para ascendente o "DESC" para descendente.
	 */
	public void ordenarLista(String columna, String sentido);
	
	//  -------------    Metodos para la Navegacion ---------------- 
	public void sigPagina();    
    public void antPagina();
    
    // Seleccion
	public void setObjetosSeleccionados(List objetosSeleccionados);
	public List getObjetosSeleccionados();
	
	
	// Getters y Setters 
	public boolean isRenderizarAnterior();	
	public boolean isRenderizarSiguiente();
	
	public int getPagina();
	public void setPagina(int pagina);

	public int getMaxResultados();
	public void setMaxResultados(int maxResultados);
    
    public int getNumPaginas();
	public List<SelectItem> getListaPaginas();

	public String getTermBusqueda();
	public void setTermBusqueda(String termBusqueda);
	public void ordenarLista(String columna);
	
	public String getOrderColumn();
	public void setOrderColumn(String orderColumn);
	public String getOrderDirection();
	public void setOrderDirection(String orderDirection);

}
