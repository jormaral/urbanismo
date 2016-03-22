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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Stateless
@Name("servicioConsultasEJB3aBD")
public class ServicioConsultasEJB3aBDBean implements ServicioConsultasEJB3aBD
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @PersistenceContext
	EntityManager em;
    
    private String value;
    private String respuesta="";

    public void servicioConsultasEJB3aBD()
    {
        // implement your business logic here
        log.info("servicioConsultasEJB3aBD.servicioConsultasEJB3aBD() action called");
        statusMessages.add("servicioConsultasEJB3aBD");
    }

    
    public String getRespuesta() {
		return respuesta;
	}

    

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}


	public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
   
    public void consultaEJB3BD ()
    {
    	log.debug("[consultaEJB3BD] Entra en metodo" );
   	
		respuesta = consultaEJB3BDString();
		log.debug("[consultaEJB3BD] Respuesta:"+respuesta );
    }
    
    public String consultaEJB3BDString ()
    {
    	log.debug("[consultaEJB3BDString] Entra en metodo" );
    	
    	String resultadoConsulta="";
    	
    	List objList;
		try {
			objList = executeQueryEJB3(value);
			
			 for (Object obj : objList) {

				 resultadoConsulta+="<br>\n";
		        	resultadoConsulta+=writeObject(obj);
		        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultadoConsulta = "Error en la consulta: \n";
			resultadoConsulta+=e.getMessage();
			
		}

 
    	
		return resultadoConsulta;
    }
    
    public List<Object> executeQueryEJB3(String query) throws Exception {

        log.debug("[executeQueryEJB3] Entra en metodo" );

        


        // Trim query
        query = query.trim();
        log.debug("  Query: [" + query.substring(0, 20) + "]");

        // Create query
        Query q = em.createQuery(query);

        // Execute query
        List<Object> result = null;
        try {

            // Query or update ???
            if (query.substring(0, 6).compareToIgnoreCase("UPDATE") == 0) {

                log.debug("   Updating ... ");

                int i = q.executeUpdate();
                result = new ArrayList<Object>();
                result.add("   Modificado " + i + " registros.");

                return result;


            // Delete
            } else if (query.substring(0, 6).compareToIgnoreCase("DELETE") == 0) {

                log.debug("   Deleting ... ");

                int j = q.executeUpdate();
                result = new ArrayList<Object>();
                result.add("   Deleted " + j + " registros.");

                return result;

            } else {

                log.debug("   Selecting ... ");
                @SuppressWarnings("unchecked")
                List<Object> result2 = (List<Object>) q.getResultList();

                return result2;

            }
        } catch (Exception ex) {
            log.error("   Error executing query: " + ex);
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }
    
    
    private String writeObject(Object o) {
        if (o == null) {
            return "null";
        }

        Class c = o.getClass();
        String result = "";
        if (c.isArray()) {
            Object[] arrayObj = (Object[]) o;

            Object o2;

            for (int i = 0; i < arrayObj.length; i++) {
                o2 = arrayObj[i];
                Class c2 = o2.getClass();

                if (c2.isPrimitive() || c2.getName().equalsIgnoreCase("java.lang.String") || c2.getName().equalsIgnoreCase("java.lang.integer") || c2.getName().equalsIgnoreCase("java.lang.long")) {
                    result += " " + o2.toString();
                } else {

                    String aux = "";
                    Object invokeObject = null;

                    for (Method m : c2.getMethods()) {
                        if (m.getName().substring(0, 3).equalsIgnoreCase("get")) {
                            try {
                                invokeObject = m.invoke(o2, null);
                                aux = "";
                                if (invokeObject == null) {
                                    aux = "null";
                                } else if ((invokeObject.getClass().getName().equalsIgnoreCase("java.lang.long")) ||
                                        (invokeObject.getClass().getName().equalsIgnoreCase("java.lang.string")) ||
                                        (invokeObject.getClass().getName().equalsIgnoreCase("java.lang.double")) ||
                                        (invokeObject.getClass().getName().equalsIgnoreCase("java.lang.integer"))) {
                                    aux = invokeObject.toString();
                                }
                                result += m.getName() + "=" + aux + "        ";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }


            }


        } else {

            if (c.isPrimitive() || c.getName().equalsIgnoreCase("java.lang.String") || c.getName().equalsIgnoreCase("java.lang.integer") || c.getName().equalsIgnoreCase("java.lang.long")) {
                result = o.toString();
            } else {
                result = "";
                String aux = "";
                Object invokeObject = null;

                for (Method m : c.getMethods()) {
                    if (m.getName().substring(0, 3).equalsIgnoreCase("get")) {
                        try {
                            invokeObject = m.invoke(o, null);
                            aux = "";
                            if (invokeObject == null) {
                                aux = "null";
                            } else if ((invokeObject.getClass().getName().equalsIgnoreCase("java.lang.long")) ||
                                    (invokeObject.getClass().getName().equalsIgnoreCase("java.lang.string")) ||
                                    (invokeObject.getClass().getName().equalsIgnoreCase("java.lang.double")) ||
                                    (invokeObject.getClass().getName().equalsIgnoreCase("java.lang.integer"))) {
                                aux = invokeObject.toString();
                            }
                            result += m.getName() + "=" + aux + "        ";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }


        return result;
    }

}
