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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.servicios.basicos.comunes.ServicioBusquedaGenerica;

@Stateful
public class ListaGenericaBean implements ListaGenerica {
	
	/// Injeccion SEAM     
	@In
	protected EntityManager entityManager;
    
    @In (create = true) 
    ServicioBusquedaGenerica servicioBusquedaGenerica;
    
    @In (create = true)
    FacesMessages facesMessages;
    
    @Logger private Log log;
    
	/// Paginacion 
	private int pagina = 1;
	private int maxResultados = 10;
	private int numPaginas;
	private Long totalRegistros = 0L;
	private List<SelectItem> listaPaginas;
	
	private boolean ordenAscendente = true;
	
	/// Seleccion
	private List objetosSeleccionados;
	
	
	/// Busqueda 
	private String termBusqueda;
	private boolean desdeBusqueda = false;
	
	
	// Lista de objetos 
	public List listaInicial;
	private String tipoLista;
	
	private String orderColumn = "";
	private String orderDirection = "";
	

	// -----------------  Metodos genericos ------------------------- //
	
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void inicializarLista(String tipo) throws Exception {
			
		this.tipoLista = tipo;
		
		// Obtenemos el numero total de registros de la consulta (sin paginar)
		totalRegistros = (Long)entityManager.createQuery("SELECT COUNT(*) FROM " + tipoLista).getSingleResult();
		
		int primerResultado = (pagina -1) * maxResultados;
		
		// Ejecutamos la consulta (paginada)
		String consulta;
		if(orderColumn != null && !orderColumn.equalsIgnoreCase("")) {
			
			consulta = "FROM " + tipoLista + " ORDER BY " + orderColumn + " " + orderDirection;			
			
		} else {
			consulta = "FROM " + tipoLista;	
		}

		log.info("Ejecutar consulta: "+consulta);
		listaInicial = (List) entityManager.createQuery(consulta)
		.setMaxResults(maxResultados)
		.setFirstResult(primerResultado > totalRegistros ? 0 : primerResultado) 
		.getResultList();
		
		// Actualizamos los indices de resultados y paginas.
		this.actualizarPaginas();
	}
	
	/** Metodo que se encarga de buscar en todos los campos de tipo String y filtra la lista
	 * utilizando los parametros de paginacion establecidos.
	 */
	@SuppressWarnings("unchecked")
	public void buscar() throws Exception  {
	
		desdeBusqueda = true;
		
		// La busqueda nos devuelve un HashMap que contiene la lista de objetos encontrados (paginada) 
		// y el total de registros encontrados.
		HashMap busqueda = servicioBusquedaGenerica.buscarEnTodosCampos(tipoLista, termBusqueda, pagina, maxResultados);
		
		totalRegistros = (Long)busqueda.get("totalRegistros");
		listaInicial = (List) busqueda.get("resultados");
		
		// Actualizamos los indices de resultados y paginas.
		this.actualizarPaginas();
					
	}
	
	/** 
	 * Ordena la lista por una columna concreta
	 * 
	 * @param columna Nombre de la columna por la cual se va a ordenar
	 * @param sentido Sentido de ordenacion. Puede ser "ASC" para ascendente o "DESC" para descendente.
	 */
	public void ordenarLista(String columna, String sentido) {
		this.orderColumn = columna;
		this.orderDirection = sentido;
		
		actualizarLista();
	}
	
	/** 
	 * Ordena la lista por una columna concreta y en orden alterno
	 * 
	 * @param columna Nombre de la columna por la cual se va a ordenar
	 */
	public void ordenarLista(String columna) {
		this.orderColumn = columna;
		if (ordenAscendente){
			this.orderDirection = "asc";
			ordenAscendente = false;
		}
		else{
			this.orderDirection = "desc";
			ordenAscendente = true;
		}
		
		actualizarLista();
	}
	
	//  -------------    Metodos para la Navegacion ---------------- 
	
	/** Avanza una pagina y renderiza la lista **/
	public void sigPagina() {
		log.info("PAGINA ANTERIOR: " + pagina, null);
    	pagina++;
    	log.info("PAGINA ACTUAL: " + pagina, null);
    	
    	// Se realiza una nueva consulta al cambiar de pagina
    	this.actualizarLista();
    	log.info("PAGINA ACTUAL (Despues de actualizar lista): " + pagina, null);
    }
    
	/** Avanza una pagina y renderiza la lista **/
    public void antPagina() {
    	pagina--;
    	
    	// Se realiza una nueva consulta al cambiar de pagina
    	this.actualizarLista();
    }
     
    
    /** Actualiza el numero de paginas y renderiza la lista. Util cuando
     * se elimina un registro.
     */
	protected void actualizarPaginas() {
		
		Double paginasD = new Double(((float)totalRegistros) / ((float)maxResultados));
    	numPaginas = (int)Math.ceil(paginasD);
    	if(numPaginas == 0)
    		numPaginas = 1;
    	
    	if(pagina > numPaginas)
    		pagina = numPaginas;
    	
		listaPaginas = new ArrayList<SelectItem>();    	
    	for(int i=1; i <= numPaginas; i++) {
			SelectItem item = new SelectItem(i);
			listaPaginas.add(item);
		}
	}
	
	
	/** Actualiza la lista de objetos en base a los parametros de busqueda y paginacion. **/
	protected void actualizarLista() {
		try {
			if(desdeBusqueda)
				buscar();
			else
				inicializarLista(tipoLista);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** Getters y Setters **/
	public boolean isRenderizarAnterior() {
		return listaInicial != null && pagina > 1;
	}
	
	public boolean isRenderizarSiguiente() {
		return listaInicial != null && listaInicial.size() == maxResultados;
	}
	
	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
		
		this.actualizarLista();
	}

	public int getMaxResultados() {
		return maxResultados;
	}

	public void setMaxResultados(int maxResultados) {
		this.maxResultados = maxResultados;
		
		// al cambiar el maximo de resultados se actualiza la lista
		this.actualizarLista();
	}
    
    public int getNumPaginas() {
    	return numPaginas;    	
	}

	public List<SelectItem> getListaPaginas() {
	
		if(listaPaginas == null)
			listaPaginas = new ArrayList<SelectItem>();		
		return listaPaginas;
	}

	public String getTermBusqueda() {
		return termBusqueda;
	}

	public void setTermBusqueda(String termBusqueda) {
		this.termBusqueda = termBusqueda;
	}
	
	public boolean isDesdeBusqueda() {
		return desdeBusqueda;
	}

	public void setDesdeBusqueda(boolean desdeBusqueda) {
		this.desdeBusqueda = desdeBusqueda;
	}

	public void setObjetosSeleccionados(List objetosSeleccionados) {
		this.objetosSeleccionados = objetosSeleccionados;
	}

	public List getObjetosSeleccionados() {
		return objetosSeleccionados;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}
}
