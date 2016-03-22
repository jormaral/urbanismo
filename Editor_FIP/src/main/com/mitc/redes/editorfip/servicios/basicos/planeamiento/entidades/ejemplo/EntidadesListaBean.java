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

package com.mitc.redes.editorfip.servicios.basicos.planeamiento.entidades.ejemplo;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

@Stateless
@Name("entidadesLista")
public class EntidadesListaBean implements EntidadesLista
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @PersistenceContext
	EntityManager entityManager;
    
    @DataModel(scope=ScopeType.PAGE)
	private List<Entidad> listaEntidades;
	

    @Factory("listaEntidades")
    public void findListaEntidades()
    {
    	listaEntidades = entityManager.createQuery("select ent from Entidad ent order by ent.iden ASC").setMaxResults(100).getResultList();
    }
    
    
    
    public void entidadesLista()
    {
        // implement your business logic here
        log.info("entidadesLista.entidadesLista() action called");
        statusMessages.add("entidadesLista");
    }



	public List<Entidad> getListaEntidades() {
		return listaEntidades;
	}



    // add additional action methods
    

}
