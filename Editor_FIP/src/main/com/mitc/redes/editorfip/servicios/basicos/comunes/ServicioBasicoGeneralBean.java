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


package com.mitc.redes.editorfip.servicios.basicos.comunes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.TokenUtils;

@Stateless

@Name("servicioBasicoGeneral")
public class ServicioBasicoGeneralBean implements ServicioBasicoGeneral
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @PersistenceContext
	EntityManager entityManager;

    public void servicioBasicoGeneral()
    {
        // implement your business logic here
        log.info("servicioBasicoAmbitos.servicioBasicoPlanes() action called");
        statusMessages.add("servicioBasicoPlanes");
    }

    public List<Object[]> findPlanesBase() {

    	List<Object[]> lista = new ArrayList<Object[]>();
		
		String queryPlanBase = "SELECT planBase.iden,planBase.nombre " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesBase plan " +
        " JOIN plan.plan planBase";
		
		lista = entityManager.createQuery(queryPlanBase).getResultList();
        
        return lista;
    }
    
    public TokenUtils obtenerTokenUtilPorNombre(String iden) {

    	List<TokenUtils> lista = new ArrayList<TokenUtils>();
		String queryPlanBase = "SELECT tokenutils " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.gestionfip.TokenUtils tokenutils " +
        " WHERE tokenutils.identificador='" + iden + "'";
		
		lista = entityManager.createQuery(queryPlanBase).getResultList();
        
		if (lista!=null && lista.size()>0)
			return lista.get(0);
		else
			return null;
    }

	@Override
	public String obtenerListaVistasPlaneamientoPorAmbito() {
		
		String resultado = "";
				
		List<Integer> listaAmbito = new ArrayList<Integer>();
		
		String queryPlanBase = "SELECT amb.iden " +
        " FROM Ambito amb ";
		
		listaAmbito = (List<Integer>)entityManager.createQuery(queryPlanBase).getResultList();
		
		for (Integer amb:listaAmbito)
		{
			resultado+="CREATE OR REPLACE VIEW planeamiento.ambito_"+amb+" AS "+ 
			" SELECT entidades_capa.identidad, entidades_capa.claveentidadbase, entidades_capa.identidadbase, entidades_capa.etiqueta, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom "+
			" FROM planeamiento.entidades_capa entidades_capa "+
			" WHERE entidades_capa.idambito = "+amb+";";
			
			resultado+="\n";
		}
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("c:\\plantillaVistas.txt"));
			out.write(resultado);
			out.close();
		}
		catch (IOException e)
		{
			System.out.println("Exception ");

		}
		
		return "a";
		
		
	}


}
