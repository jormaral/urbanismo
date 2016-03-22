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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
/**
 *
 * @author Miguel.Martin
 */
public class ClsOperacionEntidad {
    public static EntityManager gEM;
    private static final Logger mLog = Logger.getLogger(ClsOperacionEntidad.class);

    public static void OperacionesEntidades(Operacionentidad iOperacionEntidad){
        int idTipoOperacion;
        Entidad iEntO;
        Entidad iEntR;
        String s;
        int idTramiteOrdenante;
        String codPlanO;
        String codPlanR;
        int idOperacion;
        Plan iPlanOrdenante;
        Plan iPlanO;
        Plan iPlanR;
        int ordenPlanOrdenante;
        int ordenO;
        int ordenR;
        int idTramiteO;
        int idTramiteR;
        boolean ejecutarOperacion;
        String sNoEjecutar="";
        
        try{
            idTipoOperacion=(int) iOperacionEntidad.getIdtipooperacionent();
            iEntO=(Entidad) ClsConexion.dameEntity(Entidad.class, ClsMain.idEntidadPorIdOperacionEntidad(iOperacionEntidad.getIden()));
            iEntR=(Entidad) ClsConexion.dameEntity(Entidad.class, ClsMain.idEntidadOperadoraPorIdOperacionEntidad(iOperacionEntidad.getIden()));

            idOperacion=ClsMain.idOperacionPorIdOperacionEntidad(iOperacionEntidad.getIden());
            idTramiteOrdenante=ClsMain.idTramitePorIdOperacion(idOperacion);
            
            // Se elimina la operación antes de ejecutarla, para evitar el "aviso" de que se ha eliminado
            //  una operación que aún no ha sido ejecutada.
            ClsMain.eliminarOperacion(idOperacion);

            codPlanR=ClsMain.codigoPlanPorIdEntidad(iEntR.getIden());
            codPlanO=ClsMain.codigoPlanPorIdEntidad(iEntO.getIden());
            
            // El orden del plan ordenante debe ser mayor o igual que los órdenes de las entidades
            iPlanOrdenante=(Plan) ClsConexion.dameEntity(Plan.class, ClsMain.idPlanPorIdTramite(idTramiteOrdenante));
            ordenPlanOrdenante=ClsMain.ordenPlanPorIden(iPlanOrdenante.getIden());
            idTramiteO=ClsMain.idTramitePorIdEntidad(iEntO.getIden());
            iPlanO= (Plan) ClsConexion.dameEntity(Plan.class, ClsMain.idPlanPorIdTramite(idTramiteO));
            ordenO=ClsMain.ordenPlanPorIden(iPlanO.getIden());
            idTramiteR=ClsMain.idTramitePorIdEntidad(iEntR.getIden());
            iPlanR= (Plan) ClsConexion.dameEntity(Plan.class, ClsMain.idPlanPorIdTramite(idTramiteR));
            ordenR=ClsMain.ordenPlanPorIden(iPlanR.getIden());
            ejecutarOperacion=true;
            if (ordenPlanOrdenante<ordenO || ordenPlanOrdenante<ordenR){
                ejecutarOperacion=false;
                sNoEjecutar="No se ejecuta la operación porque el orden del plan ordenante es menor que el orden del plan ";
                if (ordenPlanOrdenante<ordenO){
                    sNoEjecutar=sNoEjecutar + "operado. ";
                } else {
                    sNoEjecutar=sNoEjecutar + "operador. ";
                }
                sNoEjecutar=sNoEjecutar + "Probablemente, una de las entidades ha sufrido una operación previa que la ha movido a otro plan.";
            }

            s="(" + String.valueOf(idOperacion) + ") ";
            if (!iEntR.isBsuspendida()){
                switch (idTipoOperacion) {
                    case 1:;
                        // Eliminación
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Eliminación --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_Eliminacion(iEntO, iEntR, idTramiteOrdenante);
                        break;

                    case 2:;
                        // Acumulación completa
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Acumulación Completa --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_AcumulacionCompleta(iEntO, iEntR);
                        break;

                    case 3:;
                        // Adición Normativa
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Adición Normativa --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_AdicionNormativa(iEntO, iEntR);
                        break;

                    case 4:;
                        // Adición Gráfica
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Adición Gráfica --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_AdicionGrafica(iEntO, iEntR);
                        break;

                    case 5:;
                        // Sustracción Gráfica
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Sustracción Gráfica --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SustraccionGrafica(iEntO, iEntR, idTramiteOrdenante);
                        break;

                    case 6:;
                        // Sustitución Normativa Completa
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Sustitución Normativa Completa --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SustitucionNormativaCompleta(iEntO, iEntR, idTramiteOrdenante);
                        break;

                    case 7:;
                        // Sustitución Gráfica
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Sustitución Gráfica --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SustitucionGrafica(iEntO, iEntR);
                        break;

                    case 8:;
                        // Suspensión Total
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Suspensión Total --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SuspensionTotal(iEntO, iEntR);
                        break;

                    case 9:;
                        // Sustitución Normativa Parcial
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Sustitución Normativa Parcial --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SustitucionNormativaParcial(iEntO, iEntR, idTramiteOrdenante);
                        break;

                    case 10:;
                        // Superposición Completa
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Superposición Completa --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SuperposicionCompleta(iEntO, iEntR);
                        break;

                    case 11:;
                        // Aportación de Entidad
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Aportación de Entidad --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_AportacionEntidad(iEntO, iEntR);
                        break;

                    case 12:;
                        // Acumulación de Normas Generales
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Acumulación de Normas Generales --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_AcumulacionNormasGenerales(iEntO, iEntR);
                        break;

                    case 13:;
                        // Acumulación de Usos
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Acumulación de Usos --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_AcumulacionUsos(iEntO, iEntR);
                        break;

                    case 14:;
                        // Acumulación de Actos
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Acumulación de Actos --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_AcumulacionActos(iEntO, iEntR);
                        break;

                    case 15:;
                        // Creación Gráfica
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Creación Gráfica --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_CreacionGrafica(iEntO, iEntR);
                        break;

                    case 16:;
                        // Aportación de Entidad Incorporada
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Aportación de Entidad Incorporada (no implementada) --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        break;

                    case 17:;
                        // Superposición de Normas Generales
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Superposición de Normas Generales --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SuperposicionNormasGenerales(iEntO, iEntR);
                        break;

                    case 18:;
                        // Superposición de Usos
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Superposición de Usos --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SuperposicionUsos(iEntO, iEntR);
                        break;

                    case 19:;
                        // Superposición de Actos
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Superposición de Actos --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SuperposicionActos(iEntO, iEntR);
                        break;

                    case 20:;
                        // Destrucción Gráfica
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Destrucción Gráfica --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_DestruccionGrafica(iEntO, iEntR);
                        break;

                    case 21:;
                        // Herencia de Clave
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Herencia de Clave --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_HerenciaClave(iEntO, iEntR, idTramiteOrdenante);
                        break;

                    case 22:;
                        // Suspensión Parcial
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Suspensión Parcial --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_SuspensionParcial(iEntO, iEntR);
                        break;

                    case 23:;
                        // Creación de Adscripción
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Creación de Adscripción (no implementada) --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        break;

                    case 24:;
                        // Eliminación de Adscripción
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Eliminación de Adscripción (no implementada) --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        break;

                    case 25:;
                        // Incorporación de Entidad
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Incorporación de Entidad --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_IncorporacionEntidad(iEntO, iEntR, false);
                        break;

                    case 26:;
                        // Levantamiento Total de Suspensión
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Levantamiento Total de Suspensión --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_LevantamientoTotalSuspension(iEntO, iEntR);
                        break;

                    case 27:;
                        // Aportación de Entidad sobre Entidad Padre
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Aportación de Entidad sobre Entidad Padre --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_AportacionEntidadSobrePadre(iEntO, iEntR);
                        break;

                    case 28:;
                        // Aportación de Entidad sobre Entidad Anterior
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Aportación de Entidad sobre Entidad Anterior --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_AportacionEntidadSobreAnterior(iEntO, iEntR);
                        break;

                    case 29:;
                        // Levantamiento Parcial de Suspensión
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Levantamiento Parcial de Suspensión --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_LevantamientoParcialSuspension(iEntO, iEntR);
                        break;
                        
                    case 30:;
                        // Incorporación de Trámite por Entidad
                        mLog.info(s + "[" + iEntR.getCodigo() + "] del plan [" + codPlanR+ "] --> Incorporación de Trámite por Entidad --> [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                        if (ejecutarOperacion==false){
                            mLog.warn(sNoEjecutar);
                            break;
                        }
                        opEnt_IncorporacionEntidad(iEntO, iEntR, true);
                        break;
                   
                    default:
                        mLog.warn("*** " + s + "         No se opera. La operación iden=" + idTipoOperacion + " no está implementada en esta versión.");
                }
            }else {
                mLog.warn("*** " + s + "         No se opera. La entidad operadora [" + iEntR.getCodigo() + "] está suspendida.");
            }

        } catch (Exception e){
            mLog.error("Error en OperacionEntidad: " + e + ". Código 23130.");
        }

        // Recolección de basura
        System.gc();

    }

    private static void opEnt_Eliminacion(Entidad iEntO, Entidad iEntR, int idTramiteOrdenante){
        // Se efectúan las siguientes acciones relacionadas con la entidad iEntO que
        //  se va a eliminar:

        // 1 - Reasigna los idPadre de los hijos, al idPadre de la entidad.
        // 2 - Se elimina la entidad.

        String s;
        int nRegistros;
        
        try {
            // 1.
            s = "Update planeamiento.Entidad Set idpadre=" +
                    ClsMain.idPadrePorIdEntidad(iEntO.getIden()) + " " +
                    "Where idpadre=" + iEntO.getIden();
            nRegistros = ClsConexion.ejecutar(s);

            // 2. 
            ClsMain.eliminarObjeto("Entidad", iEntO.getIden(), idTramiteOrdenante);

        } catch (Exception e) {
            mLog.error("Fallo en la operación opEnt_Eliminacion. " + e + ". Código 23101." );
        }
    }

    private static void opEnt_AcumulacionCompleta(Entidad iEntO, Entidad iEntR){
        // No se tiene en cuenta el carácter de la determinación
        // Se añaden al recinto operado, las determinaciones del operador que no
        //   le sean comunes.
        // Se llama a la función CopiarRecintoDeterminacion() con el argumento False de superposición

        List <Integer> listaIdEntDet;
        String s;
       
        try{
            // Crea la lista de EntidadDeterminacion
            listaIdEntDet=new ArrayList();
            s= "Select ed.iden " +
                    "From planeamiento.Entidaddeterminacion ed " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0 ) " +
                    "And ed.iddeterminacion Not In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntO.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0)";
            listaIdEntDet=ClsConexion.recordList(s);

            if(listaIdEntDet.size()>0){
                ClsMain.copiarEntidadDeterminacion(iEntO, listaIdEntDet, false, 0);
            }

        } catch (Exception e) {
            mLog.error("Fallo en la operación opEnt_AcumulacionCompleta. " + e + ". Código 23102." );
        }

    }

    private static void opEnt_AdicionNormativa(Entidad iEntO, Entidad iEntR){
        String s="";
        List lista;
        Iterator it;
        Entidaddeterminacion iEntDet;

        // Se borran los valores de iEntO que sean comunes a iEntR, y después se
        //  copian todos los valores de iEntR a iEntO

        try{
            // Borrado de los valores comunes.
            s = "Select iden From planeamiento.Entidaddeterminacion " +
                        "Where identidad=" + iEntO.getIden() + " " +
                        "And iddeterminacion In (Select iddeterminacion " +
                        "From planeamiento.Entidaddeterminacion " +
                        "Where identidad=" + iEntR.getIden() + ") ";
            lista=ClsConexion.recordList(s);
            it=lista.iterator();
            while (it.hasNext()){
                iEntDet=(Entidaddeterminacion) ClsConexion.dameEntity(Entidaddeterminacion.class, it.next());
                ClsMain.eliminarEntidadDeterminacion(iEntDet);
            }

            // Copia de valores de iEntR en iEntO
            s = "Select iden From planeamiento.Entidaddeterminacion " +
                        "Where identidad=" + iEntR.getIden();
            lista=ClsConexion.recordList(s);
            if (lista.size()>0){
                ClsMain.copiarEntidadDeterminacion(iEntO, lista, false, 0);
            }
        } catch (Exception e){
            mLog.error("Fallo en la operación opEnt_AdicionNormativa. " + e + ". Código 23103." );
        }
    }

    private static void opEnt_AdicionGrafica(Entidad iEntO, Entidad iEntR){
        String s="";
        String tipoGeomO="";
        String tipoGeomR="";
        Geometry geomO = null;
        Geometry geomR = null;
        Geometry geomUnion;
        int srid;
        int nRegistros;

        try{
            // Comprueba si las entidades tienen geometría y, además, son del
            //  mismo tipo.
            tipoGeomO=tieneGeometria(iEntO);
            tipoGeomR=tieneGeometria(iEntR);

            if (tipoGeomO.equals("") || tipoGeomR.equals("")){
                mLog.error("Error: no se puede efectuar la operación de Adición Gráfica porque, al menos, una entidad no tiene geometría. Código 23123.");
                return;
            }
            if (tipoGeomO.equals("Error") || tipoGeomR.equals("Error")){
                mLog.error("Error: no se puede efectuar la operación de Adición Gráfica porque, al menos, una entidad tiene más de una geometría. Código 23124.");
                return;
            }
            if (!tipoGeomO.equals(tipoGeomR)){
                mLog.error("Error: no se puede efectuar la operación de Adición Gráfica porque las entidades tiene distinto tipo de geometría. Código 23126.");
                return;
            }

            // Recupera la geometría de iEntO
            geomO=ClsMain.geometria(tipoGeomO, iEntO.getIden(), false);
            if (geomO==null){
                s="Aviso: no se puede efectuar la operación de Adición Gráfica " + 
                    "porque la entidad operada no tiene geometría o está suspendida.";
                mLog.warn(s);
                return;
            }
            
            // Recupera la geometría de iEntR
            geomR=ClsMain.geometria(tipoGeomR, iEntR.getIden(), false);
            if (geomR==null){
                s="Aviso: no se puede efectuar la operación de Adición Gráfica " + 
                    "porque la entidad operadora no tiene geometría o está suspendida.";
                mLog.warn(s);
                return;
            }

            // Ejecuta la unión de las dos geometrías
            srid=ClsMain.srId(tipoGeomO, iEntO.getIden());
            geomUnion=ClsMain.operarGeometrias(geomO, geomR, 1);
            geomUnion=ClsMain.limpiarGeometria(tipoGeomO, geomUnion);
            nRegistros=0;
            if((tipoGeomO.equalsIgnoreCase("ENTIDADPOL") && (geomUnion.getGeometryType().equalsIgnoreCase("MULTIPOLYGON") || 
                            geomUnion.getGeometryType().equalsIgnoreCase("POLYGON")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADLIN") && (geomUnion.getGeometryType().equalsIgnoreCase("MULTILINESTRING") || 
                            geomUnion.getGeometryType().equalsIgnoreCase("LINESTRING")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADPNT") && (geomUnion.getGeometryType().equalsIgnoreCase("MULTIPOINT") || 
                            geomUnion.getGeometryType().equalsIgnoreCase("POINT")))
                            ){
            s="Update planeamiento." + tipoGeomO + " Set geom=multi(geometryfromtext('" +
                geomUnion + "', " + srid + ")) " + 
                "Where identidad=" + iEntO.getIden() + " And bsuspendida=false ";
            nRegistros=ClsConexion.ejecutar(s);
            }
            if (nRegistros!=1){
                s="***     No se ha conseguido actualizar la geometría " +
                    "de tipo " + geomUnion.getGeometryType() + " en la tabla " + tipoGeomO + " con la instrucción <" +
                    s + ">";
                mLog.warn(s);
            }

            // Descarga variables
            geomO = null;
            geomR = null;
            geomUnion = null;

        } catch (Exception e){
            mLog.error("Fallo en la operación opEnt_AdicionGrafica. " + e + ". Código 23104." );
        }
    }

    public static void opEnt_SustraccionGrafica(Entidad iEntO, Entidad iEntR, int idTramiteOrdenante){
        String s="";
        String tipoGeomO="";
        String tipoGeomR="";
        Geometry geomO = null;
        Geometry geomR = null;
        Geometry geomDif;
        int srid;
        int nRegistros;
        String codPlanO;

        try{
            // Comprueba si las entidades tienen geometría y, además, son del
            //  tipo adecuado:
            //      iEntR --> polígono
            //      iEntO --> polígono, punto, o línea
            tipoGeomO=tieneGeometria(iEntO);
            tipoGeomR=tieneGeometria(iEntR);

            if (tipoGeomO.equals("") || tipoGeomR.equals("")){
                mLog.error("Error: no se puede efectuar la operación de Sustracción Gráfica porque, al menos, una entidad no tiene geometría. Código 23123.");
                return;
            }
            if (tipoGeomO.equals("Error") || tipoGeomR.equals("Error")){
                mLog.error("Error: no se puede efectuar la operación de Sustracción Gráfica porque, al menos, una entidad tiene más de una geometría. Código 23124.");
                return;
            }
            if (!tipoGeomR.equalsIgnoreCase("Entidadpol")){
                mLog.error("Error: no se puede efectuar la operación de Sustracción Gráfica porque la entidad operadora no es un polígono. Código 23126.");
                return;
            }

            // Código del plan operado (para mensajes)
            codPlanO=ClsMain.codigoPlanPorIdEntidad(iEntO.getIden());
            
            // Averigua el SRID
            srid=ClsMain.srId(tipoGeomO, iEntO.getIden());

            // Recupera la geometría de iEntO
            geomO=ClsMain.geometria(tipoGeomO, iEntO.getIden(), false);
            if (geomO==null){
                s="Aviso: no se puede efectuar la operación de Sustracción Gráfica " + 
                    "porque la entidad operada no tiene geometría o está suspendida.";
                mLog.warn(s);
                return;
            }
            
            // Recupera la geometría de iEntR
            geomR=ClsMain.geometria(tipoGeomR, iEntR.getIden(), false);
            if (geomO==null){
                s="Aviso: no se puede efectuar la operación de Sustracción Gráfica " + 
                    "porque la entidad operadora no tiene geometría o está suspendida.";
                mLog.warn(s);
                return;
            }

            // Ejecuta la diferencia de las dos geometrías
            geomDif=ClsMain.operarGeometrias(geomO, geomR, 3);
            geomDif=ClsMain.limpiarGeometria(tipoGeomO, geomDif);

            if(geomDif==null){
                // Si no queda ningún trozo de geometría, se elimina la entidad completa.
                mLog.warn("***     El resultado de la sustracción es una geometría nula. Se elimina la entidad [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                opEnt_Eliminacion(iEntO, iEntR, idTramiteOrdenante);
            } else {
                if(!geomDif.isEmpty()){
                    nRegistros=0;
                    if((tipoGeomO.equalsIgnoreCase("ENTIDADPOL") && (geomDif.getGeometryType().equalsIgnoreCase("MULTIPOLYGON") || 
                            geomDif.getGeometryType().equalsIgnoreCase("POLYGON")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADLIN") && (geomDif.getGeometryType().equalsIgnoreCase("MULTILINESTRING") || 
                            geomDif.getGeometryType().equalsIgnoreCase("LINESTRING")))
                            ||
                            (tipoGeomO.equalsIgnoreCase("ENTIDADPNT") && (geomDif.getGeometryType().equalsIgnoreCase("MULTIPOINT") || 
                            geomDif.getGeometryType().equalsIgnoreCase("POINT")))
                            ){
                        s="Update planeamiento." + tipoGeomO + " Set geom=multi(geometryfromtext('" +
                            geomDif + "', " + srid + ")) " + 
                            "Where identidad=" + iEntO.getIden() + " And bsuspendida=false ";
                        nRegistros=ClsConexion.ejecutar(s);
                    }
                    if (nRegistros!=1){
                        s="***     No se ha conseguido actualizar la geometría " +
                            "de tipo " + geomDif.getGeometryType() + " en la tabla " + tipoGeomO + " con la instrucción <" +
                            s + ">";
                        mLog.warn(s);
                    }

                } else {
                    // Si no queda ningún trozo de geometría, se elimina la entidad completa.
                    mLog.warn("***     El resultado de la sustracción es una geometría vacía. Se elimina la entidad [" + iEntO.getCodigo() + "] del plan [" + codPlanO + "]");
                    opEnt_Eliminacion(iEntO, iEntR, idTramiteOrdenante);
                }
            }

            // Descarga variables
            geomO = null;
            geomR = null;
            geomDif = null;

        } catch (Exception e){
            mLog.error("Fallo en la operación opEnt_SustraccionGrafica. " + e + ". Código 23105." );
        }

    }

    private static void opEnt_SustitucionNormativaCompleta(Entidad iEntO, Entidad iEntR, int idTramiteOrdenante){
        sustitucionNormativa(iEntO, iEntR, true, idTramiteOrdenante);
        
    }

    private static void opEnt_SustitucionGrafica(Entidad iEntO, Entidad iEntR){
        try{
            opEnt_DestruccionGrafica(iEntO, iEntR);
            opEnt_CreacionGrafica(iEntO, iEntR);
        } catch (Exception e) {
            mLog.error("Fallo en la operación opEnt_SustitucionGrafica. " + e + ". Código 23107." );
        }

    }

    private static void opEnt_SuspensionTotal(Entidad iEntO, Entidad iEntR){
        // Pone la marca bSuspendida=true en todas las geometrías de iEntO, y en la propia entidad.
        String s;
        int nRegistros=0;

        try {
            s = "Update planeamiento.EntidadPol Set bsuspendida=true " +
                    "Where identidad In (Select iden From planeamiento.Entidad " +
                    "Where iden=" + iEntO.getIden() + ") ";
            nRegistros = nRegistros + ClsConexion.ejecutar(s);
            s = "Update planeamiento.EntidadPnt Set bsuspendida=true " +
                    "Where identidad In (Select iden From planeamiento.Entidad " +
                    "Where iden=" + iEntO.getIden() + ") ";
            nRegistros = nRegistros + ClsConexion.ejecutar(s);
            s = "Update planeamiento.EntidadLin Set bsuspendida=true " +
                    "Where identidad In (Select iden From planeamiento.Entidad " +
                    "Where iden=" + iEntO.getIden() + ") ";
            nRegistros = nRegistros + ClsConexion.ejecutar(s);
            if (nRegistros>0){
                mLog.warn("        Se han suspendido " + nRegistros + " geometrías en la entidad [" + iEntO.getCodigo() + "]");
            }
            s = "Update planeamiento.Entidad Set bsuspendida=true " +
                    "Where iden=" + iEntO.getIden() + " ";
            nRegistros=ClsConexion.ejecutar(s);
            if (nRegistros==1){
                mLog.warn("        Se ha suspendido totalmente la entidad [" + iEntO.getCodigo() + "]");
            } else {
                mLog.warn("Aviso: no se ha logrado suspender la normativa de la entidad [" + iEntO.getCodigo() + "]");
            }

            // Por último, se llama al procedimiento que agrupa geometrías por el campo "bSuspendida"
            ClsMain.agruparGeometrias(iEntO);

        } catch (Exception e) {
            mLog.error("Error: fallo en la operación opEnt_SuspensionTotal. " + e + ". Código 23108." );
        }

    }

    private static void opEnt_SustitucionNormativaParcial(Entidad iEntO, Entidad iEntR, int idTramiteOrdenante){
        sustitucionNormativa(iEntO, iEntR, false, idTramiteOrdenante);

    }

    private static void opEnt_SuperposicionCompleta(Entidad iEntO, Entidad iEntR){
        // No se tiene en cuenta el carácter de la determinación
        // Se añaden al recinto operado, las determinaciones del operador que no
        //   le sean comunes.
        // Se llama a la función CopiarRecintoDeterminacion() con el argumento TRUE de superposición, y
        //  con el máximo valor encontrado de superposición

        List <Integer> listaIdEntDet;
        int superposicion;
        String s;

        try{
            // Crea la lista de EntidadDeterminacion
            listaIdEntDet=new ArrayList();
            s= "Select ed.iden " +
                    "From planeamiento.Entidaddeterminacion ed " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0 ) " +
                    "And ed.iddeterminacion Not In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntO.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0)";
            listaIdEntDet=ClsConexion.recordList(s);

            superposicion=ClsMain.maximaSuperposicionDeEntidad(iEntO.getIden());

            if(listaIdEntDet.size()>0){
                ClsMain.copiarEntidadDeterminacion(iEntO, listaIdEntDet, true, superposicion);
            }
        } catch (Exception e){
            mLog.error("Fallo en la operación opEnt_SuperposicionCompleta. " + e + ". Código 23110." );
        }
    }

    public static void opEnt_AportacionEntidad(Entidad iEntO, Entidad iEntR){
        // Se cambia el idTramite de la entidad iEntR, y se cambia su idPadre para
        //  que apunte a la entidad "Entidades aportadas por..." que se
        //  debe crear en el trámite de iEntO
        Entidad iEntPadre;
        int idTramiteO;
        int idTramiteR;
        
        try{
            idTramiteO=ClsMain.idTramitePorIdEntidad(iEntO.getIden());
            idTramiteR=ClsMain.idTramitePorIdEntidad(iEntR.getIden());

            if(idTramiteO!=idTramiteR){
                // Entidad de la que va a colgar
                iEntPadre = crearCarpetaEntidadesAportadas(idTramiteO, idTramiteR);
                // Reasignar el IdTramite
                bReasignarEntidad(iEntO, iEntPadre, iEntR, 0);
            }

        } catch (Exception e) {
            mLog.error("Fallo en la operación opEnt_AportacionEntidad. " + e + ". Código 23111." );
        }
        
    }

    private static void opEnt_AcumulacionNormasGenerales(Entidad iEntO, Entidad iEntR){
        // Se tiene en cuenta el carácter de la determinación. Sólo se contemplan
        //  las de carácter "Norma General Literal" y "Norma General Gráfica"
        // Se añaden al recinto operado, las determinaciones del operador que no
        //   le sean comunes.
        // Se llama a la función CopiarRecintoDeterminacion() con el argumento False de superposición

        List <Integer> listaIdEntDet;
        String s;

        try {
            // Crea la lista de EntidadDeterminacion
            listaIdEntDet = new ArrayList();
            s = "Select ed.iden " +
                    "From planeamiento.Entidaddeterminacion ed, planeamiento.Determinacion d " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion=d.iden " +
                    "And (d.idcaracter=" + ClsDatos.ID_CARACTER_NORMAGENERALLITERAL + " " +
                    "Or d.idcaracter=" + ClsDatos.ID_CARACTER_NORMAGENERALGRAFICA + ") " +
                    "And ed.iddeterminacion In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0 ) " +
                    "And ed.iddeterminacion Not In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntO.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0)";
            listaIdEntDet = ClsConexion.recordList(s);

            if (listaIdEntDet.size() > 0) {
                ClsMain.copiarEntidadDeterminacion(iEntO, listaIdEntDet, false, 0);
            }
        } catch (Exception e) {
            mLog.error("Fallo en la operación opEnt_AcumulacionNormasGenerales. " + e + ". Código 23112." );
        }
    }

    private static void opEnt_AcumulacionUsos(Entidad iEntO, Entidad iEntR){
        // Se tiene en cuenta el carácter de la determinación. Sólo se contemplan
        //  las de carácter "Uso" y "Grupo de Usos"
        // Se añaden al recinto operado, las determinaciones del operador que no
        //   le sean comunes.
        // Se llama a la función CopiarRecintoDeterminacion() con el argumento False de superposición

        List <Integer> listaIdEntDet;
        String s;

        try {
            // Crea la lista de EntidadDeterminacion
            listaIdEntDet = new ArrayList();
            s = "Select ed.iden " +
                    "From planeamiento.Entidaddeterminacion ed, planeamiento.Determinacion d " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion=d.iden " +
                    "And (d.idcaracter=" + ClsDatos.ID_CARACTER_USO + " " +
                    "Or d.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEUSOS + ") " +
                    "And ed.iddeterminacion In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0 ) " +
                    "And ed.iddeterminacion Not In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntO.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0)";
            listaIdEntDet = ClsConexion.recordList(s);

            if (listaIdEntDet.size() > 0) {
                ClsMain.copiarEntidadDeterminacion(iEntO, listaIdEntDet, false, 0);
            }
        } catch (Exception e) {
            mLog.error("Fallo en la operación opEnt_AcumulacionUsos. " + e + ". Código 23113." );
        }
    }

    private static void opEnt_AcumulacionActos(Entidad iEntO, Entidad iEntR){
        // Se tiene en cuenta el carácter de la determinación. Sólo se contemplan
        //  las de carácter "Acto de Ejecución" y "Grupo de Actos"
        // Se añaden al recinto operado, las determinaciones del operador que no
        //   le sean comunes.
        // Se llama a la función CopiarRecintoDeterminacion() con el argumento False de superposición

        List <Integer> listaIdEntDet;
        String s;

        try {
            // Crea la lista de EntidadDeterminacion
            listaIdEntDet = new ArrayList();
            s = "Select ed.iden " +
                    "From planeamiento.Entidaddeterminacion ed, planeamiento.Determinacion d " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion=d.iden " +
                    "And (d.idcaracter=" + ClsDatos.ID_CARACTER_ACTODEEJECUCION + " " +
                    "Or d.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEACTOS + ") " +
                    "And ed.iddeterminacion In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0 ) " +
                    "And ed.iddeterminacion Not In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntO.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0)";
            listaIdEntDet = ClsConexion.recordList(s);

            if (listaIdEntDet.size() > 0) {
                ClsMain.copiarEntidadDeterminacion(iEntO, listaIdEntDet, false, 0);
            }
        } catch (Exception e) {
            mLog.error("Fallo en la operación opEnt_AcumulacionActos. " + e + ". Código 23114." );
        }
    }

    private static void opEnt_CreacionGrafica(Entidad iEntO, Entidad iEntR){
        String s;
        int nRegistros;

        // Se copian los registros gráficos de iEntR a iEntO. Primero se
        //  comprueba que iEntO no tenga previamente ninguna geometría, y
        //  que iEntR tenga alguna.
        try {
            if (!ClsOperacionEntidad.tieneGeometria(iEntO).equals("")){
                s="Aviso: no se puede efectuar la operación de Creación Gráfica " + 
                    "porque la entidad operada ya tiene geometría.";
                mLog.warn(s);
                return;
            }
            
            if (ClsOperacionEntidad.tieneGeometria(iEntR).equals("")){
                s="Aviso: no se puede efectuar la operación de Creación Gráfica " + 
                    "porque la entidad operadora no tiene geometría.";
                mLog.warn(s);
                return;
            }
            
            nRegistros=0;
            s = "Insert Into planeamiento.Entidadpol (identidad, geom, bsuspendida) " +
                    "Select " + iEntO.getIden() + ", geom, false " +
                    "From planeamiento.Entidadpol " + 
                    "Where identidad=" + iEntR.getIden() + " And bsuspendida=false ";
            nRegistros = nRegistros+ClsConexion.ejecutar(s);
            s = "Insert Into planeamiento.Entidadlin (identidad, geom, bsuspendida) " +
                    "Select " + iEntO.getIden() + ", geom, false " +
                    "From planeamiento.Entidadlin " + 
                    "Where identidad=" + iEntR.getIden() + " And bsuspendida=false ";
            nRegistros = nRegistros+ClsConexion.ejecutar(s);
            s = "Insert Into planeamiento.Entidadpnt (identidad, geom, bsuspendida) " +
                    "Select " + iEntO.getIden() + ", geom, false " +
                    "From planeamiento.Entidadpnt " + 
                    "Where identidad=" + iEntR.getIden() + " And bsuspendida=false ";
            nRegistros = nRegistros+ClsConexion.ejecutar(s);
            if (nRegistros!=1){
                s="***     No se ha conseguido actualizar la geometría " +
                    "con la instrucción <" + s + ">";
                mLog.warn(s);
            }
        } catch (Exception e) {
            mLog.error("Fallo en la operación opEnt_CreacionGrafica. " + e + ". Código 23115." );
        }

    }

    private static void opEnt_SuperposicionNormasGenerales(Entidad iEntO, Entidad iEntR){
        // Se tiene en cuenta el carácter de la determinación. Sólo se contemplan
        //  las de carácter "Norma General Literal" y "Norma General Gráfica"
        // Se añaden al recinto operado, las determinaciones del operador que no
        //   le sean comunes.
        // Se llama a la función CopiarRecintoDeterminacion() con el argumento TRUE de superposición, y
        //  con el máximo valor encontrado de superposición

        List <Integer> listaIdEntDet;
        int superposicion;
        String s;

        try{
            // Crea la lista de EntidadDeterminacion
            listaIdEntDet=new ArrayList();
            s= "Select ed.iden " +
                    "From planeamiento.Entidaddeterminacion ed, planeamiento.Determinacion d " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion=d.iden " +
                    "And (d.idcaracter=" + ClsDatos.ID_CARACTER_NORMAGENERALLITERAL + " " +
                    "Or d.idcaracter=" + ClsDatos.ID_CARACTER_NORMAGENERALGRAFICA + ") " +
                    "And ed.iddeterminacion In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0 ) " +
                    "And ed.iddeterminacion Not In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntO.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0)";
            listaIdEntDet=ClsConexion.recordList(s);

            superposicion=ClsMain.maximaSuperposicionDeEntidad(iEntO.getIden());

            if(listaIdEntDet.size()>0){
                ClsMain.copiarEntidadDeterminacion(iEntO, listaIdEntDet, true, superposicion);
            }
        } catch (Exception e){
            mLog.error("Fallo en la operación opEnt_SuperposicionNormasGenerales. " + e + ". Código 23117." );
        }
    }

    private static void opEnt_SuperposicionUsos(Entidad iEntO, Entidad iEntR){
        // Se tiene en cuenta el carácter de la determinación. Sólo se contamplan
        //  las de carácter "Uso" y "Grupo de Usos"
        // Se añaden al recinto operado, las determinaciones del operador que no
        //   le sean comunes.
        // Se llama a la función CopiarRecintoDeterminacion() con el argumento TRUE de superposición, y
        //  con el máximo valor encontrado de superposición

        List <Integer> listaIdEntDet;
        int superposicion;
        String s;

        try{
            // Crea la lista de EntidadDeterminacion
            listaIdEntDet=new ArrayList();
            s= "Select ed.iden " +
                    "From planeamiento.Entidaddeterminacion ed, planeamiento.Determinacion d " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion=d.iden " +
                    "And (d.idcaracter=" + ClsDatos.ID_CARACTER_USO + " " +
                    "Or d.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEUSOS + ") " +
                    "And ed.iddeterminacion In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0 ) " +
                    "And ed.iddeterminacion Not In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntO.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0)";
            listaIdEntDet=ClsConexion.recordList(s);

            superposicion=ClsMain.maximaSuperposicionDeEntidad(iEntO.getIden());

            if(listaIdEntDet.size()>0){
                ClsMain.copiarEntidadDeterminacion(iEntO, listaIdEntDet, true, superposicion);
            }
        } catch (Exception e){
            mLog.error("Fallo en la operación opEnt_SuperposicionUsos. " + e + ". Código 23118." );
        }
    }

    private static void opEnt_SuperposicionActos(Entidad iEntO, Entidad iEntR){
        // Se tiene en cuenta el carácter de la determinación. Sólo se contamplan
        //  las de carácter "Acto de Ejecución" y "Grupo de Actos"
        // Se añaden al recinto operado, las determinaciones del operador que no
        //   le sean comunes.
        // Se llama a la función CopiarRecintoDeterminacion() con el argumento TRUE de superposición, y
        //  con el máximo valor encontrado de superposición

        List <Integer> listaIdEntDet;
        int superposicion;
        String s;

        try{
            // Crea la lista de EntidadDeterminacion
            listaIdEntDet=new ArrayList();
            s= "Select ed.iden " +
                    "From planeamiento.Entidaddeterminacion ed, planeamiento.Determinacion d " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion=d.iden " +
                    "And (d.idcaracter=" + ClsDatos.ID_CARACTER_ACTODEEJECUCION + " " +
                    "Or d.idcaracter=" + ClsDatos.ID_CARACTER_GRUPODEACTOS + ") " +
                    "And ed.iddeterminacion In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0 ) " +
                    "And ed.iddeterminacion Not In( " +
                    "Select ed.iddeterminacion " +
                    "From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Casoentidaddeterminacion ced " +
                    "Where ed.identidad=" + iEntO.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idcaso=ced.iden " +
                    "And edr.superposicion=0)";
            listaIdEntDet=ClsConexion.recordList(s);

            superposicion=ClsMain.maximaSuperposicionDeEntidad(iEntO.getIden());

            if(listaIdEntDet.size()>0){
                ClsMain.copiarEntidadDeterminacion(iEntO, listaIdEntDet, true, superposicion);
            }
        } catch (Exception e){
            mLog.error("Fallo en la operación opEnt_SuperposicionActos. " + e + ". Código 23119." );
        }
    }

    private static void opEnt_DestruccionGrafica(Entidad iEntO, Entidad iEntR){
        String s;
        int nRegistros;

        try{
            if (ClsOperacionEntidad.tieneGeometria(iEntO).equals("")){
                s="Aviso: no se puede efectuar la operación de Destrucción Gráfica " + 
                    "porque la entidad operada no tiene geometría.";
                mLog.warn(s);
                return;
            }
            s="Delete From planeamiento.Entidadpol Where identidad=" + iEntO.getIden();
            nRegistros = ClsConexion.ejecutar(s);
            s="Delete From planeamiento.Entidadlin Where identidad=" + iEntO.getIden();
            nRegistros = ClsConexion.ejecutar(s);
            s="Delete From planeamiento.Entidadpnt Where identidad=" + iEntO.getIden();
            nRegistros = ClsConexion.ejecutar(s);
        } catch (Exception e) {
            mLog.error("Fallo en la operación opEnt_DestruccionGrafica. " + e + ". Código 23120." );
        }
    }

    private static void opEnt_HerenciaClave(Entidad iEntO, Entidad iEntR, int idTramiteOrdenante){
        String s;
        List <Integer> listaIdRelacion;
        List <Integer> lista;
        Iterator it;
        Boolean tieneHerencia;
        int nRegistros;
        int iden;

        try{
            // Primero comprueba si la entidad operadora ya tiene aplicada la relación
            //  de 'Herencia de clave'. Si la tiene, actualiza su valor, y si no la tiene,
            //  la crea nueva.
            // La defrelación es la ID_DEFRELACION_HERENCIACLAVE
            listaIdRelacion=new ArrayList();
            listaIdRelacion=ClsMain.relacionesPorElemento("ENTIDAD", iEntO.getIden());
            it=listaIdRelacion.iterator();
            tieneHerencia=false;
            while (it.hasNext()){
                s="Select iden From planeamiento.Relacion " +
                        "Where iddefrelacion=" + ClsDatos.ID_DEFRELACION_HERENCIACLAVE + " " +
                        "And iden=" + it.next();
                lista=ClsConexion.recordList(s);
                if (lista.size()>0){
                    // La entidad tiene una relación de Herencia de clave
                    tieneHerencia=true;
                    s="Update planeamiento.PropiedadRelacion Set valor='" + iEntR.getClave() + "' " +
                            "Where idRelacion=" + lista.get(0);
                    nRegistros=ClsConexion.ejecutar(s);
                }
            }

            if (tieneHerencia==false){
                // Inserta la nueva relación
                s="Insert Into planeamiento.Relacion (iddefrelacion, idtramitecreador) " +
                        "Values(" + ClsDatos.ID_DEFRELACION_HERENCIACLAVE + ", " +
                        idTramiteOrdenante + ") ";
                nRegistros=ClsConexion.ejecutar(s);
                iden=ClsMain.ultimoIden("planeamiento.Relacion");
                // Inserta el vector que apunta a la entidad operada
                s="Insert Into planeamiento.Vectorrelacion (idrelacion, iddefvector, valor) " +
                        "Values (" + iden + ", " + ClsDatos.ID_DEFVECTOR_HERENCIACLAVE + ", " +
                        iEntO.getIden() + ") ";
                nRegistros=ClsConexion.ejecutar(s);
                // Inserta la propiedad que contiene el valor=clave de la entidad operadora
                s="Insert Into planeamiento.Propiedadrelacion (idrelacion, iddefpropiedad, valor) " +
                        "Values (" + iden + ", " + ClsDatos.ID_DEFPROPIEDAD_HERENCIACLAVE + ", '" +
                        iEntR.getClave() + "') ";
                nRegistros=ClsConexion.ejecutar(s);
            }
        } catch (Exception e){
            mLog.error("Fallo en la operación opEnt_HerenciaClave. " + e + ". Código 23121." );
        }

    }

    public static void opEnt_SuspensionParcial(Entidad iEntO, Entidad iEntR){
        suspensionYlevantamientoParcial(iEntO, iEntR, true);
    }

    public static boolean opEnt_IncorporacionEntidad(Entidad iEntO, Entidad iEntR, boolean bEsIncorporacionPlan){
        String s;
        String tipoGeomO="";
        String tipoGeomR="";
        String tipoGeom="";
        List listaO;
        Iterator itO;
        boolean bSuspendidaO;
        List lista;
        Iterator it;
        Geometry geomO = null;
        Geometry geomR = null;
        Geometry geomInter;
        Geometry geom1;
        Geometry geom2;
        Geometry geomDif;
        WKTReader reader;
        String geomWKT;
        int srid;
        int nRegistros;
        Entidad iEntPadre;
        int idEntPadre;
        Entidad iEnt;
        Determinacion iDetGrupo;
        int idEntidad;
        int idTramiteO;
        int idTramiteR;
        Tramite iTramiteO;
        Tramite iTramiteR;
        Object[] obj;
        boolean incorporadoOK=false;
        String codigoEntAnt;
        String codigoPlanAnt;
        List listaIdenEntOperadas;
        Iterator itlistaIdenEntOperadas;
        Entidad iEntAAo;
        boolean bRecortada;
        boolean bRecortadaAlMenosUna;
        boolean bRecortar;
        String txtEntidad;
        String codPlan;
        Plan iP;
        
        // Las geometrías suspendidas también deben incorporarse. 
        // Entidad incorporada    : iEntO
        // Entidad incorporadora  : iEntR
        
        try{
            listaIdenEntOperadas=new ArrayList();
            // Si la operación es de incorporación de plan, se añaden los iden de 
            //  todas las entidades del plan incorporado. Si es una incorporación
            //  de una entidad individual, sólo se añade el iden de ésta.
            // Las entidades de esta lista, más la entidad operadora, no serán cortadas
            //  cuando se corten las entidades del plan operador.
            if (bEsIncorporacionPlan==false){
                listaIdenEntOperadas.add(iEntO.getIden());
                mLog.info("    Incorporación de la entidad [" + iEntO.getCodigo() + "] " + 
                    "del plan [" + ClsMain.codigoPlanPorIdEntidad(iEntO.getIden()) + 
                    "] por la entidad [" + iEntR.getCodigo() + "] del " + 
                    "plan [" + ClsMain.codigoPlanPorIdEntidad(iEntR.getIden()) +"]");
            } else {
                idTramiteO=ClsMain.idTramitePorIdEntidad(iEntO.getIden());
                iTramiteO=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteO);
                iEntAAo=ClsMain.ambitoAplicacionPorTramite(iTramiteO);
                
                // De la lista de entidades a incorporar, se excluyen: 
                //      - El Ámbito de Aplicación.
                //      - Las entidades sin geometría.
                s="Select E.iden From planeamiento.Entidad E " + 
                    "Where E.idTramite=" + idTramiteO + " " + 
                    "And E.iden<>" + iEntAAo.getIden() + " " + 
                    "And E.iden In (Select EPOL.idEntidad " +
                    "From planeamiento.EntidadPol EPOL Union " + 
                    "Select ELIN.idEntidad From planeamiento.EntidadLin ELIN " +
                    "Union Select EPNT.idEntidad From planeamiento.EntidadPnt EPNT) " +
                    "Order By E.codigo ";
                listaIdenEntOperadas = ClsConexion.recordList(s);
                
                mLog.info("    Incorporación del plan [" + ClsMain.codigoPlanPorIdEntidad(iEntO.getIden()) + 
                    "] por la entidad [" + iEntR.getCodigo() + "] del " + 
                    "plan [" + ClsMain.codigoPlanPorIdEntidad(iEntR.getIden()) +"]");
                mLog.info("        Entidades a incorporar:");
                itlistaIdenEntOperadas=listaIdenEntOperadas.iterator();
                while (itlistaIdenEntOperadas.hasNext()){
                    idEntidad=Integer.parseInt(itlistaIdenEntOperadas.next().toString());
                    iEntO=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidad);
                    mLog.info("        [" + iEntO.getCodigo() + "] (" + iEntO.getClave() + 
                        ") " + iEntO.getNombre());
                }
                mLog.info("");
            }
            
            itlistaIdenEntOperadas=listaIdenEntOperadas.iterator();
            while (itlistaIdenEntOperadas.hasNext()){
                idEntidad=Integer.parseInt(itlistaIdenEntOperadas.next().toString());
                iEntO=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidad);
                iDetGrupo=ClsMain.determinacionGrupoPorEntidad(iEntO);
                s="        Incorporando entidad: [" + iEntO.getCodigo() + "] ";
                mLog.info(s);
                
                codigoEntAnt=iEntO.getCodigo();
                codigoPlanAnt=ClsMain.codigoPlanPorIdEntidad(iEntO.getIden());
                idTramiteO=ClsMain.idTramitePorIdEntidad(iEntO.getIden());
                iTramiteO=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteO);
                idTramiteR=ClsMain.idTramitePorIdEntidad(iEntR.getIden());
                iTramiteR=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteR);

                // Agrupa las geometrías, para garantizar la correcta ejecución de
                //  la operación.
                ClsMain.agruparGeometrias(iEntO);
                ClsMain.agruparGeometrias(iEntR);

                // Comprueba si las entidades tienen geometría y, además, son del
                //  tipo adecuado: la incorporadora siempre debe ser poligonal, y la 
                //  incorporada puede ser de cualquier tipo.
                tipoGeomO=tieneGeometria(iEntO);
                tipoGeomR=tieneGeometria(iEntR);

                if (tipoGeomO.equals("") || tipoGeomR.equals("")){
                    mLog.warn("Aviso: no se puede efectuar la operación de Incorporación porque, al menos, una entidad no tiene geometría.");
                    continue;
                }
                if (tipoGeomO.equals("Error") || tipoGeomR.equals("Error")){
                    mLog.warn("Aviso: no se puede efectuar la operación de Incorporación porque, al menos, una entidad tiene más de un tipo de geometría.");
                    continue;
                }
                if (!tipoGeomR.equalsIgnoreCase("Entidadpol")){
                    mLog.warn("Aviso: no se puede efectuar la operación de Incorporación porque la entidad incorporadora no es poligonal.");
                    continue;
                }

                // Recupera la geometría de iEntR que no esté suspendida.
                geomR=ClsMain.geometria(tipoGeomR, iEntR.getIden(), false);
                if (geomR==null){
                    mLog.warn("Aviso: no se puede efectuar la operación de Incorporación porque la entidad incorporadora tiene su geometría suspendida.");
                    continue;
                }

                // Recupera las geometrías de iEntO.
                reader = new WKTReader();
                s="Select astext(geom), bsuspendida From planeamiento." + tipoGeomO + " " + 
                    "Where identidad=" + iEntO.getIden();
                listaO = ClsConexion.recordList(s);
                itO=listaO.iterator();

                incorporadoOK=false;
                while (itO.hasNext()){
                    obj=(Object[]) itO.next();
                    bSuspendidaO=Boolean.parseBoolean(obj[1].toString());
                    geomWKT=obj[0].toString();
                    try {
                        geomO = reader.read(geomWKT);
                    } catch (ParseException e) {
                        mLog.error("Error al leer la geometría de la entidad iden=" + iEntO.getIden() + ". " + e + ". Código 23127.");
                        continue;
                    }

                    // Ejecuta la intersección de las dos geometrías
                    srid=ClsMain.srId(tipoGeomR, iEntR.getIden());
                    geomInter=ClsMain.operarGeometrias(geomO, geomR, 2);
                    geomInter=ClsMain.limpiarGeometria(tipoGeomO, geomInter);

                    if (geomInter==null && bSuspendidaO==false){
                        // No se ejecuta la incorporación porque las entidades no intersectan
                        continue;
                    }
                    if(geomInter.isEmpty() && bSuspendidaO==false){
                        // No se ejecuta la incorporación porque las entidades no intersectan
                        continue;
                    }

                    nRegistros=0;
                    if((tipoGeomO.equalsIgnoreCase("ENTIDADPOL") && (geomInter.getGeometryType().equalsIgnoreCase("MULTIPOLYGON") || 
                                geomInter.getGeometryType().equalsIgnoreCase("POLYGON")))
                                ||
                                (tipoGeomO.equalsIgnoreCase("ENTIDADLIN") && (geomInter.getGeometryType().equalsIgnoreCase("MULTILINESTRING") || 
                                geomInter.getGeometryType().equalsIgnoreCase("LINESTRING")))
                                ||
                                (tipoGeomO.equalsIgnoreCase("ENTIDADPNT") && (geomInter.getGeometryType().equalsIgnoreCase("MULTIPOINT") || 
                                geomInter.getGeometryType().equalsIgnoreCase("POINT")))
                                ){
                        s="Update planeamiento." + tipoGeomO + " Set geom=multi(geometryfromtext('" +
                            geomInter + "', " + srid + ")) Where identidad=" + iEntO.getIden() + " " +
                            "And bsuspendida=" + bSuspendidaO;
                        nRegistros=ClsConexion.ejecutar(s);
                        if (nRegistros>=1){
                            incorporadoOK=true;
                        }
                    }
                }

                if (incorporadoOK==true){
                    // Reasigna el trámite a la entidad incorporada iEntO, poniéndole el
                    //  de la entidad incorporadora iEntR.
                    iEntPadre=crearCarpetaEntidadesAportadas(idTramiteR, idTramiteO);
                    bReasignarEntidad(iEntR, iEntPadre, iEntO, 0);

                    // Recorta las entidades del trámite incorporador cuyo grupo (valor
                    //  para la determinación 'Grupo de entidades' sea el grupo de la
                    //  entidad incorporada.
                    // Se excluyen: la entidad incorporadora y las contenidas en 
                    //  la lista de entidades para incorporar.
                    //  También se excluyen aquellas entidades que hayan sido 
                    //  previamente incorporadas al plan incorporador, y siempre que la 
                    //  carpeta que las contenga pertenezca a un plan de orden 
                    //  anterior, de modo que sólo queden excluídas las entidades
                    //  aportadas por la operación de incorporación, y no las que
                    //  provengan de desarrollos o aportaciones. La carpeta 'padre'
                    //  debe ser una de las 'Aportadas por...'
                    geom1=ClsMain.geometria(tipoGeomO, iEntO.getIden(), false);
                    iDetGrupo=ClsMain.determinacionGrupoPorEntidad(iEntO);
                    lista=new ArrayList();
                    lista=ClsMain.idEntidadesPorGrupoTramite(iDetGrupo, iTramiteR);
                    it=lista.iterator();
                    mLog.info("        Recortando entidades del Grupo: " + iDetGrupo.getNombre());
                    bRecortadaAlMenosUna=false;
                    while (it.hasNext()){
                        idEntidad=Integer.parseInt(it.next().toString());
                        iEnt=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidad);
                        tipoGeom=tieneGeometria(iEnt);            

                        if (!tipoGeom.equalsIgnoreCase("") && !tipoGeom.equalsIgnoreCase("ERROR")){
                            // Sólo se recortan las entidades diferentes de las incorporadas y de
                            //  la incorporadora.
                            if (!listaIdenEntOperadas.contains(idEntidad) && idEntidad!=iEntR.getIden()){
                                // Comprueba si la entidad está alojada en una 
                                //  carpeta 'Aportadas por...'. Se busca la carpeta de
                                //  más alto nivel en el árbol, ya que la entidad puede
                                //  haber sido aportada indirectamente, por ser hija de
                                //  una que haya sido directamente aportada.
                                bRecortar=true;
                                idEntPadre=ClsMain.idPadrePorIdEntidad(idEntidad);
                                while(idEntPadre>0) {
                                    iEntPadre=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntPadre);
                                    txtEntidad = ClsDatos.TEXTO_APORTADAS; // Sólo es el comienzo del nombre
                                    if (iEntPadre.getNombre().startsWith(txtEntidad)){
                                        // Es una entidad carpeta con el nombre 'Aportadas por...'
                                        codPlan=iEntPadre.getNombre().substring(1+txtEntidad.length(), 6+txtEntidad.length());
                                        // Con este código de plan hay que averiguar si su orden es 
                                        //  anterior al plan incorporador, en cuyo caso, la
                                        //  entidad idEntidad no debe ser recortada.                                
                                        s="Select Count(*) From planeamiento.plan P " + 
                                            "Where P.codigo='" + codPlan + "' " +
                                            "And P.orden<=" + ClsMain.ordenPlanPorIden(ClsMain.idPlanPorIdTramite(
                                            ClsMain.idTramitePorIdEntidad(idEntPadre)));
                                        lista=new ArrayList();
                                        lista=ClsConexion.recordList(s);
                                        s=lista.get(0).toString();
                                        if (Integer.parseInt(s)>0){
                                            // La entidad NO debe ser recortada
                                            s="            No se recorta la entidad [" + iEnt.getCodigo() + "] " + 
                                                "porque ya fue aportada por el plan [" + codPlan + "]";
                                            mLog.info(s);
                                            bRecortar=false;
                                            break;
                                        }
                                    }
                                    idEntPadre=ClsMain.idPadrePorIdEntidad(iEntPadre.getIden());
                                }
                                if (bRecortar==false){
                                    continue;
                                }

                                // Averigua si las entidades se solapan. Si no es así, no se operan.
                                bRecortada=false;
                                //  - Parte suspendida
                                geom2=ClsMain.geometria(tipoGeom, idEntidad, true);
                                if (geom2!=null){
                                    geomInter=ClsMain.operarGeometrias(geom1, geom2, 2);
                                    if (geomInter!=null){
                                        if (geomInter.isEmpty()==false){
                                            bRecortada=true;
                                        }
                                    }
                                }
                                //  - Parte no suspendida
                                geom2=ClsMain.geometria(tipoGeom, idEntidad, false);
                                if (geom2!=null){
                                    geomInter=ClsMain.operarGeometrias(geom1, geom2, 2);
                                    if (geomInter!=null){
                                        if (geomInter.isEmpty()==false){
                                            bRecortada=true;
                                        }
                                    }
                                }
                                // Si las entidades no se solapan, no se continúa con el recorte.
                                if (bRecortada==false){
                                    continue;
                                }
                                
                                iDetGrupo=ClsMain.determinacionGrupoPorEntidad(iEnt);
                                s="            Recortando [" + iEnt.getCodigo() + "] (" + 
                                    iDetGrupo.getNombre() + ") del plan [" +
                                    ClsMain.codigoPlanPorIdEntidad(idEntidad) + "]";
                                mLog.info(s);

                                // Averigua el srid
                                srid=ClsMain.srId(tipoGeomR, iEntR.getIden());
                                
                                // Controla si la entidad se ha recortado
                                bRecortada=false;

                                // Parte suspendida
                                geom2=ClsMain.geometria(tipoGeom, idEntidad, true);
                                if (geom2!=null){
                                    geomDif=ClsMain.operarGeometrias(geom2, geom1, 3);
                                    geomDif=ClsMain.limpiarGeometria(tipoGeom, geomDif);
                                    if (geomDif!=null){
                                        s="Update planeamiento." +tipoGeom + " Set geom=multi(geometryfromtext('" +
                                        geomDif + "', " + srid + ")) Where identidad=" + idEntidad + " " +
                                        "And bsuspendida=true";
                                        nRegistros=ClsConexion.ejecutar(s);
                                        if (nRegistros>0){
                                            bRecortada=true;
                                        }
                                    }
                                }
                                // Parte no suspendida
                                geom2=ClsMain.geometria(tipoGeom, idEntidad, false);
                                if (geom2!=null){
                                    geomDif=ClsMain.operarGeometrias(geom2, geom1, 3);
                                    geomDif=ClsMain.limpiarGeometria(tipoGeom, geomDif);
                                    if (geomDif!=null){
                                        s="Update planeamiento." + tipoGeom + " Set geom=multi(geometryfromtext('" +
                                        geomDif + "', " + srid + ")) Where identidad=" + idEntidad + " " +
                                        "And bsuspendida=false";
                                        nRegistros=ClsConexion.ejecutar(s);
                                        if (nRegistros>0){
                                            bRecortada=true;
                                        }
                                    }
                                }
                                
                                // Si no se ha conseguido recortar la entidad, se envía un mensaje.
                                if (bRecortada==false){
                                    s="                No se ha conseguido recortar la entidad.";
                                    mLog.info(s);
                                } else {
                                    bRecortadaAlMenosUna=true;
                                }

                                // Si a la entidad no le queda geometria, hay que eliminarla.
                                iEnt=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidad);
                                if (tieneGeometria(iEnt).equalsIgnoreCase("")){
                                    s="            Se elimina la entidad [" + iEnt.getCodigo() + "] " +
                                        "del plan [" + ClsMain.codigoPlanPorIdEntidad(iEntR.getIden()) + "]";
                                    mLog.info(s);
                                    ClsMain.eliminarObjeto("Entidad", idEntidad, idTramiteR);
                                }
                            }
                        } else {
                            // La entidad del plan incorporador no tiene geometría.
                        }
                    }

                    // Mensajes de incorporación.
                    if (bRecortadaAlMenosUna==false){
                        s="            No hay entidades para recortar.";
                        mLog.info(s);
                    }
                    idEntidad=iEntO.getIden();
                    iEnt=(Entidad) ClsConexion.dameEntity(Entidad.class, idEntidad);
                    s="        Incorporada en el plan [" + 
                        ClsMain.codigoPlanPorIdEntidad(iEntR.getIden()) + "] " +
                        "con código de entidad [" + iEnt.getCodigo() + "]";
                    mLog.info(s);
                    mLog.info("");
                
                } else {
                    s="        No intersecta con la entidad [" + iEntR.getCodigo() + "] " + 
                            "del plan [" + ClsMain.codigoPlanPorIdEntidad(iEntR.getIden()) + "] ";
                    mLog.info(s);
                    mLog.info("");
                }
            }
            return true;
            
        } catch (Exception e){
            if (bEsIncorporacionPlan==false){
                mLog.error("Fallo en la operación de incorporacion de Entidad. " + e + ". Código 23125." );
            } else {
                mLog.error("Fallo en la operación de incorporacion de Plan. " + e + ". Código 23125." );
            }
            mLog.info("");
            return false;
        }
        
    }
    
    private static void opEnt_LevantamientoTotalSuspension(Entidad iEntO, Entidad iEntR){
        // Pone la marca bSuspendida=false en todas las geometrías de iEntO, y en la propia Entidad.
        String s;
        int nRegistros=0;

        try {
            s = "Update planeamiento.EntidadPol Set bsuspendida=false " +
                    "Where identidad In (Select iden From planeamiento.Entidad " +
                    "Where iden=" + iEntO.getIden() + ") ";
            nRegistros = nRegistros + ClsConexion.ejecutar(s);
            s = "Update planeamiento.EntidadPnt Set bsuspendida=false " +
                    "Where identidad In (Select iden From planeamiento.Entidad " +
                    "Where iden=" + iEntO.getIden() + ") ";
            nRegistros = nRegistros + ClsConexion.ejecutar(s);
            s = "Update planeamiento.EntidadLin Set bsuspendida=false " +
                    "Where identidad In (Select iden From planeamiento.Entidad " +
                    "Where iden=" + iEntO.getIden() + ") ";
            nRegistros = nRegistros + ClsConexion.ejecutar(s);
            s = "Update planeamiento.Entidad Set bsuspendida=false " +
                    "Where iden=" + iEntO.getIden() + " ";
            s = String.valueOf(ClsConexion.ejecutar(s));

            mLog.warn("        Se ha levantado la suspensión a " + nRegistros + " geometrías.");

            // Por último, se llama al procedimiento que agrupa geometrías por el campo "bSuspendida"
            ClsMain.agruparGeometrias(iEntO);

        } catch (Exception e) {
            mLog.error("Error: fallo en la operación opEnt_LevantamientoTotalSuspension. " + e + ". Código 23131." );
        }

    }

    public static void opEnt_LevantamientoParcialSuspension(Entidad iEntO, Entidad iEntR){
        suspensionYlevantamientoParcial(iEntO, iEntR, false);
    }

    private static void opEnt_AportacionEntidadSobrePadre(Entidad iEntO, Entidad iEntR){
        // Se cambia el idTramite de la entidad iEntR, y se cambia su idPadre para
        //  que apunte a la entidad iEntO. Se le asigna el orden 1 para que sea la primera
        //  entidad hija.
        Entidad iEntPadre;
        int idTramiteO;
        int idTramiteR;

        try {
            idTramiteO=ClsMain.idTramitePorIdEntidad(iEntO.getIden());
            idTramiteR=ClsMain.idTramitePorIdEntidad(iEntR.getIden());

            // Entidad de la que va a colgar
            iEntPadre = iEntO;
            // Reasignar el IdTramite
            bReasignarEntidad(iEntO, iEntPadre, iEntR, 1);
            
        } catch (Exception e){
            mLog.error("Fallo en la operación opDet_AportacionDeterminacionSobrePadre. " + e + ". Código 23203." );
        }

    }

    private static void opEnt_AportacionEntidadSobreAnterior(Entidad iEntO, Entidad iEntR){
        // Se cambia el idTramite de la entidad iEntR, y se cambia su idPadre para
        //  que apunte a la entidad padre de iEntO. El orden que se le asigna es el siguiente al
        //  orden que tiene iEntO
        Entidad iEntPadre;
        int idTramiteO;
        int idTramiteR;
        int orden;

        try {
            idTramiteO=ClsMain.idTramitePorIdEntidad(iEntO.getIden());
            idTramiteR=ClsMain.idTramitePorIdEntidad(iEntR.getIden());

            // Entidad de la que va a colgar
            iEntPadre = iEntO.getEntidadByIdpadre();
            // Orden que se le va a asignar
            orden = 1 + ClsMain.ordenEntidadPorIden(iEntO.getIden());
            // Reasignar el IdTramite
            bReasignarEntidad(iEntO, iEntPadre, iEntR, orden);

        } catch (Exception e){
            mLog.error("Fallo en la operación opDet_AportacionDeterminacionSobrePadre. " + e + ". Código 23203." );
        }

    }
    
    public static boolean bReasignarEntidad(Entidad iEntO, Entidad iEntPadre, Entidad iEntR, int orden){
        // Reasignar el idTramite de la entidad iEntR al de iEntO, cambiando su
        //  código para evitar la restricción que impide la duplicación de códigos en
        //  un mismo trámite.

        // Primero, se averigua cuál es el último código de entidad en
        //  el trámite iTramiteO.
        // Segundo, se reasigna el trámite de iEntR desde iTramiteR a iTramiteO

        // Se elimina el registro de PlanEntidadOrdenacion si los planes origen y
        //  destino son diferentes

        // También se cambia el idTramite de sus elementos dependientes:
        //      - Entidades hijas
        //      - Documentos
        //      - Valores (determinaciones aplicadas, excepto la
        //          determinación 'Grupo de Entidades', determinaciones de
        //          régimen, y los valores de referencia). La determinación 'Grupo
        //          de Entidades' aplicada y su valor de referencia se cambian
        //          por sus equivalentes en el nuevo trámite iTramiteO (siempre 
        //          que no sean del plan base).

        // El parámetro "orden" indica el orden que se le debe asignar a la entidad. Si es cero, significa
        //  que debe asignarse el último + 1. En cualquier caso, el orden del resto de entidades debe
        //  moverse para acomodar iEntR en su sitio.

        String maxCod;
        Iterator it;        
        Documento iDocumento;
        Entidad iEnt;
        Tramite iTramiteO;
        Tramite iTramiteR;
        Plan iPlanR;
        String s;
        int nRegistros;
        Determinacion iDetPadre;
        Determinacion iDet;
        Determinacion iDetP;
        Determinacion iDetGrupoDeEntidadesR;
        Determinacion iDetGrupoDeEntidadesO;
        Determinacion iDetGrupoVR;
        Determinacion iDetVRequivalente;
        Opciondeterminacion iOpcion;
        int idOpcion;
        String aptdo;
        Integer iden;
        int idPlanR;
        int idPlanO;
        int idTramiteO;
        int idTramiteR;
        int idDeterminacionBase;
        int idDetGrupoVR;
        List <Integer> lista;
        String codigoRant;
        int ordenPlanO;
        int ordenPlanR;
        int idTramiteDet;
        int idPlanDet;
        int ordenPlanDet;

        try {
            idTramiteO=ClsMain.idTramitePorIdEntidad(iEntO.getIden());
            idTramiteR=ClsMain.idTramitePorIdEntidad(iEntR.getIden());

            // Si los dos trámites son el mismo, se aborta el procedimiento.
            if (idTramiteO==idTramiteR){
                return true;
            }
            
            // Si alguno de los dos es un trámite base, también se aborta.
            if (ClsDatos.gListaIdTramitesBase.contains(idTramiteO)==true){
                return true;
            }
            if (ClsDatos.gListaIdTramitesBase.contains(idTramiteR)==true){
                return true;
            }
            
            iTramiteO=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteO);
            iTramiteR=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteR);
            
            idPlanO=ClsMain.idPlanPorIdTramite(iTramiteO.getIden());
            ordenPlanO=ClsMain.ordenPlanPorIden(idPlanO);
            idPlanR=ClsMain.idPlanPorIdTramite(iTramiteR.getIden());
            ordenPlanR=ClsMain.ordenPlanPorIden(idPlanR);
            iPlanR=(Plan) ClsConexion.dameEntity(Plan.class, idPlanR);

            // Averigua cuál es la determinación 'Grupo de Entidades' de
            //  ambos trámites, para ignorar la del operador a la hora de
            //  reasignar el trámite. A la entidad iEntR hay que asignarle la
            //  determinación 'Grupo de Entidades' del trámite operado. 
            iDetGrupoVR=ClsMain.determinacionGrupoPorEntidad(iEntR);
            //if (iDetGrupoVR.getTramite().getIden()==ClsDatos.g)
            iDetVRequivalente=ClsMain.determinacionGrupoEquivalentePorEntidad(iEntR, iTramiteO);
            iDetGrupoDeEntidadesR=ClsMain.determinacionGrupoDeEntidadesPorTramite(iTramiteR);
            iDetGrupoDeEntidadesO=ClsMain.determinacionGrupoDeEntidadesPorTramite(iDetVRequivalente.getTramite());
            iOpcion=ClsMain.opcionPorDeterminaciones(iDetGrupoDeEntidadesO,iDetVRequivalente);

            // Reasigna los datos de la entidad iEntR para encajarla en el trámite iTramiteO
            codigoRant=iEntR.getCodigo();
            maxCod = ClsMain.maximoCodigoEntidad(iTramiteO.getIden(), 1);
            s="Update planeamiento.Entidad Set codigo='" + maxCod + "' " +
                    "Where iden=" + iEntR.getIden() + " ";
            nRegistros=ClsConexion.ejecutar(s);

            // Se cambia el padre de la entidad
            nRegistros=0;
            if (iEntPadre==null){
                s="Update planeamiento.Entidad Set idpadre=Null " +
                    "Where iden=" + iEntR.getIden();
            } else {
                s="Update planeamiento.Entidad Set idpadre=" + iEntPadre.getIden() + " " +
                    "Where iden=" + iEntR.getIden();
            }
            nRegistros=ClsConexion.ejecutar(s);

            // Orden de la entidad
            if (orden==0){
                // Hay que poner el 1+orden de los que tienen el mismo padre
                // Se calcula el máximo orden de las que tienen el mismo padre
                if (iEntPadre==null){
                    s="Select Max(orden) From planeamiento.Entidad Where idpadre Is Null " +
                            "And idTramite=" + iTramiteO.getIden();
                } else {
                    s="Select Max(orden) From planeamiento.Entidad Where idpadre=" + iEntPadre.getIden();
                }
                lista=new ArrayList();
                lista=ClsConexion.recordList(s);
                if (lista.get(0)==null){
                    orden=1;
                } else {
                    orden=1+lista.get(0);
                }
            } else {
                // Se le suma 1 a todos los órdenes mayores o iguales al orden que se le va a asignar, y luego
                //  se le pone su orden.
                s="Update planeamiento.Entidad Set orden=orden+1 Where orden>=" + orden + " And idpadre";
                if (iEntPadre==null){
                    s = s + " Is Null And idtramite=" + iTramiteO.getIden();
                } else {
                    s = s +"=" + iEntPadre.getIden();
                }
                nRegistros=ClsConexion.ejecutar(s);
            }
            s="Update planeamiento.Entidad Set orden=" + orden + " Where iden=" + iEntR.getIden();
            nRegistros=ClsConexion.ejecutar(s);

            // Reasigna sus documentos, si los tiene
            s="Select iddocumento From planeamiento.Documentoentidad " +
                    "Where identidad=" + iEntR.getIden();
            it = ClsConexion.recordList(s).iterator();
            while (it.hasNext()) {
                iDocumento=(Documento) ClsConexion.dameEntity(Documento.class, it.next());
                s="Update planeamiento.Documento Set idtramite=" + iTramiteO.getIden() + " " +
                    "Where iden=" + iDocumento.getIden();
                nRegistros=ClsConexion.ejecutar(s);
            }

            // Elimina el registro de PlanEntidadOrdenacion si los planes origen y
            //  destino son diferentes
            if (idPlanO!=idPlanR){
                s="Delete From planeamiento.Planentidadordenacion " +
                        "Where identidadordenacion=" + iEntR.getIden();
                nRegistros=ClsConexion.ejecutar(s);
                if (nRegistros>0){
                    mLog.warn("Aviso: la entidad [" + codigoRant + "] ha dejado de ser entidad de ordenación del plan [" + iPlanR.getCodigo() +"].");
                }
            }
            
            // Reasigna sus valores
            iDetPadre=ClsOperacionDeterminacion.crearCarpetaDeterminacionesAportadas(iTramiteO.getIden(), iTramiteR.getIden());

            // Determinaciones aplicadas (excepto la determinación 'Grupo de Entidades')
            //  Sólo se tienen en cuenta las determinaciones cuyo orden de plan sea igual
            //  o inferior al de la entidad iEntR, de modo que no se reasignen aquellas que
            //  han sido reasignadas en virtud de una incorporación.
            s="Select ed.iddeterminacion From planeamiento.Entidaddeterminacion ed " +
                "Where ed.identidad=" + iEntR.getIden() + " " +
                "And ed.iddeterminacion<>" + iDetGrupoDeEntidadesR.getIden() + " ";
            it = ClsConexion.recordList(s).iterator();
            while (it.hasNext()){
                iden=Integer.parseInt(it.next().toString());
                idTramiteDet=ClsMain.idTramitePorIdDeterminacion(iden);
                idPlanDet=ClsMain.idPlanPorIdTramite(idTramiteDet);
                ordenPlanDet=ClsMain.ordenPlanPorIden(idPlanDet);
                if (ordenPlanDet<=ordenPlanR){
                    iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                    iDetP=(Determinacion) ClsConexion.dameEntity(Determinacion.class, ClsMain.idPadrePorIdDeterminacion(iden));
                    if (iDetP!=null){
                        if (ClsMain.idTramitePorIdDeterminacion(iDetP.getIden())==ClsMain.idTramitePorIdDeterminacion(iDetPadre.getIden())){
                            ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetP, iDet, 0);
                        } else {
                            ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0);
                        }
                    } else {
                        ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0);
                    }
                }
            }

            // Determinaciones de régimen. Sólo las de orden de plan igual o inferior
            //  al orden de plan de iEntR
            s="Select edr.iddeterminacionregimen From planeamiento.Entidaddeterminacion ed, " +
                "planeamiento.Casoentidaddeterminacion ced, " +
                "planeamiento.Entidaddeterminacionregimen edr " +
                "Where ed.identidad=" + iEntR.getIden() + " " +
                "And ced.identidaddeterminacion=ed.iden " +
                "And (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) " +
                "And edr.iddeterminacionregimen Is Not Null ";
            it = ClsConexion.recordList(s).iterator();
            while (it.hasNext()){
                iden= Integer.parseInt(it.next().toString());
                idTramiteDet=ClsMain.idTramitePorIdDeterminacion(iden);
                idPlanDet=ClsMain.idPlanPorIdTramite(idTramiteDet);
                ordenPlanDet=ClsMain.ordenPlanPorIden(idPlanDet);
                if (ordenPlanDet<=ordenPlanR){
                    iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class,iden);
                    ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0);
                }
            }

            //      Valores de referencia (excepto los de la determinación 'Grupo de Entidades')
            s="Select od.iddeterminacionvalorref From planeamiento.Entidaddeterminacion ed, " +
                    "planeamiento.Casoentidaddeterminacion ced, " +
                    "planeamiento.Opciondeterminacion od, " +
                    "planeamiento.Entidaddeterminacionregimen edr " +
                    "Where ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion<>" + iDetGrupoDeEntidadesR.getIden() + " " +
                    "And ced.identidaddeterminacion=ed.iden " +
                    "And edr.idopciondeterminacion=od.iden " +
                    "And (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) ";
            it = ClsConexion.recordList(s).iterator();
            while (it.hasNext()){
                iden= Integer.parseInt(it.next().toString());
                idTramiteDet=ClsMain.idTramitePorIdDeterminacion(iden);
                idPlanDet=ClsMain.idPlanPorIdTramite(idTramiteDet);
                ordenPlanDet=ClsMain.ordenPlanPorIden(idPlanDet);
                if (ordenPlanDet<=ordenPlanR){
                    iDet=(Determinacion) ClsConexion.dameEntity(Determinacion.class, iden);
                    ClsOperacionDeterminacion.bReasignarDeterminacion(iDetPadre, iDetPadre, iDet, 0);
                }
            }

            // El puntero a la determinación 'Grupo de Entidades' del iTramiteR se cambia
            //  por un puntero a la del trámite iTramiteO
            s="Update planeamiento.Entidaddeterminacion " +
                    "Set iddeterminacion=" + iDetGrupoDeEntidadesO.getIden() + " "+
                    "Where identidad=" + iEntR.getIden() + " " +
                    "And iddeterminacion=" + iDetGrupoDeEntidadesR.getIden() + " ";
            nRegistros=ClsConexion.ejecutar(s);

            // La opción que apunta al grupo de entidades de iTramiteR se cambia por su
            //  opción equivalente en iTramiteO. Si no existe tal equivalencia, hay que crear
            //  una nueva determinación en iTramiteO con los datos de la determinación
            //  valor de referencia de iTramiteR, y apuntarla a la entidad iEntR.
            if(iDetVRequivalente==null){
                // No existe la determinación equivalente, y hay que crearla
                aptdo="";
                if(iDetGrupoVR.getApartado()!=null){
                    idDetGrupoVR=iDetGrupoVR.getIden();
                    aptdo=ClsMain.ultimoApartadoDeterminacion(ClsMain.idPadrePorIdDeterminacion(idDetGrupoVR), ClsMain.idTramitePorIdDeterminacion(idDetGrupoVR), true);
                }
                idDeterminacionBase=ClsMain.idDeterminacionBasePorIdDeterminacion(iDetGrupoVR.getIden());
                if (idDeterminacionBase!=0){
                    s="Insert Into planeamiento.Determinacion (idtramite, idpadre, idcaracter, " +
                        "apartado, nombre, iddeterminacionbase, codigo, orden) " +
                        "Values (" + iTramiteO.getIden() + ", " + iDetGrupoDeEntidadesO.getIden() + ", " +
                        ClsDatos.ID_CARACTER_VALORREFERENCIA + ", '" + aptdo + "', '" +
                        iDetGrupoVR.getNombre() +"', " + idDeterminacionBase + ", '" +
                        ClsMain.maximoCodigoDeterminacion(iTramiteO.getIden(), 1) + "', " + 
                        "(Select 1+Max(orden) From planeamiento.Determinacion " + 
                        "Where idTramite=" + iTramiteO.getIden() + " " + 
                        "And idPadre=" + iDetGrupoDeEntidadesO.getIden() + ")) ";
                } else {
                    s="Insert Into planeamiento.Determinacion (idtramite, idpadre, idcaracter, " +
                        "apartado, nombre, iddeterminacionbase, codigo, orden) " +
                        "Values (" + iTramiteO.getIden() + ", " + iDetGrupoDeEntidadesO.getIden() + ", " +
                        ClsDatos.ID_CARACTER_VALORREFERENCIA + ", '" + aptdo + "', '" +
                        iDetGrupoVR.getNombre() +"', Null, '" +
                        ClsMain.maximoCodigoDeterminacion(iTramiteO.getIden(), 1) + "', " + 
                        "(Select 1+Max(orden) From planeamiento.Determinacion " + 
                        "Where idTramite=" + iTramiteO.getIden() + " " + 
                        "And idPadre=" + iDetGrupoDeEntidadesO.getIden()  + ")) ";
                }
                nRegistros=ClsConexion.ejecutar(s);
                iDetVRequivalente=(Determinacion) ClsConexion.dameEntity(Determinacion.class, ClsMain.ultimoIden("planeamiento.Determinacion"));
                iOpcion=ClsMain.opcionPorDeterminaciones(iDetGrupoDeEntidadesO,iDetVRequivalente);
            }

            if(iOpcion==null){
                // La opción no existe y hay que crearla
                s="Insert Into planeamiento.Opciondeterminacion (iddeterminacion, " +
                        "iddeterminacionvalorref) Values (" + iDetGrupoDeEntidadesO.getIden() + ", " +
                        iDetVRequivalente.getIden() + ") ";
                idOpcion=ClsMain.ultimoIden("planeamiento.Opciondeterminacion");
                iOpcion=(Opciondeterminacion) ClsConexion.dameEntity(Opciondeterminacion.class, idOpcion);
            }
            s="Update planeamiento.Entidaddeterminacionregimen " +
                    "Set idopciondeterminacion=" + iOpcion.getIden() + " " +
                    "Where iden In (Select edr.iden From planeamiento.Entidaddeterminacionregimen edr, " +
                    "planeamiento.Entidaddeterminacion ed, planeamiento.Casoentidaddeterminacion ced " +
                    "Where (edr.idcaso=ced.iden Or edr.idcasoaplicacion=ced.iden) " +
                    "And ced.identidaddeterminacion=ed.iden And ed.identidad=" + iEntR.getIden() + " " +
                    "And ed.iddeterminacion=" + iDetGrupoDeEntidadesO.getIden() + ") ";
            nRegistros=ClsConexion.ejecutar(s);

            // Por último, cambia el trámite y las entidades hijas
            s="Update planeamiento.Entidad Set idtramite=" + iTramiteO.getIden() + " " +
                    "Where iden=" + iEntR.getIden();
            nRegistros=ClsConexion.ejecutar(s);
            // Sus hijas, si las tiene
            s="Select iden From planeamiento.Entidad Where idpadre=" + iEntR.getIden();
            it = ClsConexion.recordList(s).iterator();
            while (it.hasNext()) {
                iEnt=(Entidad) ClsConexion.dameEntity(Entidad.class, it.next());
                bReasignarEntidad(iEntO, iEntR, iEnt, 0);
            }
            
        } catch (Exception e) {
            mLog.error("Fallo en reasignarEntidad. " + e + ". Código 23026." );
            return false;
        }

        return true;
    }

    public static Entidad crearCarpetaEntidadesAportadas(int idTramiteO, int idTramiteR){
        // Crea la carpeta de "Entidades aportadas" en el trámite iTramiteO con
        //  el nombre del trámite iTramiteR, si no existe en iTramiteO.
        //   De ella, se colgarán todas las entidades del iTramiteR que deban
        //  ser reasignadas al iTramiteO

        String s;
        String txtEntidad;
        Entidad iEntidad = null;
        Tramite iTramiteO;
        Tramite iTramiteR;
        int idEnt;
        String codigoEnt;
        int nRegistros;
        List lista;
        Determinacion iDetGrupoDeEntidades;
        Determinacion iDetCarpeta;
        Opciondeterminacion iOpcion;
        int idOpcion;
        int idEntDet;
        int idCaso;
        Plan iPlanR;
        int idPlanR;
        Plan iPlanO;
        int idPlanO;
        Iterator it1;
        int idTramite;
        Vector v;
        Tramite iTramiteBase;

        try {
            iTramiteO=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteO);
            idPlanO=ClsMain.idPlanPorIdTramite(idTramiteO);
            iPlanO=(Plan) ClsConexion.dameEntity(Plan.class, idPlanO);

            iTramiteR=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramiteR);

            if (iTramiteR==null){
                // El trámite ya no existe por haber sido eliminado en alguna operación. No obstante, la carpeta
                //  correspondiente a sus documentos puede existir. Para averiguarlo, se recorre la lista de
                //  documentos guardados.
                it1=ClsDatos.gListaDatosDoc.iterator();
                txtEntidad="";
                while (it1.hasNext()){
                    // Recupera el texto de la entidad 'Aportadas por...' que se guardó en gListaDatosDoc para
                    //  el idTtramiteR
                    v=new Vector();
                    v=(Vector) it1.next();
                    idTramite=Integer.parseInt(v.get(0).toString());
                    if (idTramite==idTramiteR){
                        txtEntidad=v.get(1).toString();
                        break;
                    }
                }
                // Si no se ha encontrado el trámite
                if (txtEntidad.equals("")){
                    return null;
                }
            } else {
                idPlanR=ClsMain.idPlanPorIdTramite(idTramiteR);
                iPlanR=(Plan) ClsConexion.dameEntity(Plan.class, idPlanR);
                txtEntidad = ClsDatos.TEXTO_APORTADAS + ClsMain.datosPlan(iPlanR);
            }

            // Se limita la longitud del nombre de carpeta a 100 caracteres
            if (txtEntidad.length() > 100) {
                txtEntidad = txtEntidad.substring(0, 100);
            }

            // Averigua si la entidad ya existe por haber sido creada con anterioridad
            lista=new ArrayList();
            s = "Select e.iden " +
                    "From planeamiento.Entidad as e " +
                    "Where e.idtramite=" + idTramiteO + " " +
                    "And e.nombre='" + txtEntidad + "' ";
            lista = ClsConexion.recordList(s);
            if (lista.size() == 0) {
                codigoEnt = ClsMain.maximoCodigoEntidad(idTramiteO, 1);
                s = "Insert Into planeamiento.Entidad (idtramite, idpadre, clave, " +
                        "nombre, codigo, bsuspendida) Values (" + idTramiteO + ", " +
                        "null, '" + iPlanO.getCodigo() + "', '" + txtEntidad + "', '" + codigoEnt + "', false) ";
                nRegistros = ClsConexion.ejecutar(s);
                idEnt = ClsMain.ultimoIden("planeamiento.Entidad");
                iEntidad=(Entidad) ClsConexion.dameEntity(Entidad.class, idEnt);
            } else {
                idEnt = Integer.parseInt(lista.get(0).toString());
                iEntidad=(Entidad) ClsConexion.dameEntity(Entidad.class, idEnt);
                return iEntidad;
            }

            // Añadir su valor para la determinación 'Grupo de entidades' (es la determinación 'Carpeta', lo que anteriormente era el grupo 'Grupo')
            iTramiteBase=ClsMain.tramiteBasePorTramite(iTramiteO);
            iDetGrupoDeEntidades=ClsMain.determinacionGrupoDeEntidadesPorTramite(iTramiteBase);
            iDetCarpeta=ClsMain.determinacionCarpetaTramiteBase(iTramiteO);
            if (iDetGrupoDeEntidades==null | iDetCarpeta==null){
                mLog.warn("    No se ha encontrado ni se ha podido crear la carpeta de 'Entidades aportadas'" );
                return null;
            }
            iOpcion=ClsMain.opcionPorDeterminaciones(iDetGrupoDeEntidades, iDetCarpeta);
            idOpcion=iOpcion.getIden();

            // Inserta el valor en las tablas de valores
            s="Insert Into planeamiento.Entidaddeterminacion " +
                    "(identidad, iddeterminacion) " +
                    "Values (" + idEnt + ", " + iDetGrupoDeEntidades.getIden() + ")";
            nRegistros = ClsConexion.ejecutar(s);
            idEntDet = ClsMain.ultimoIden("planeamiento.Entidaddeterminacion");
            s="Insert Into planeamiento.Casoentidaddeterminacion (identidaddeterminacion) " +
                    "Values (" + idEntDet + ") ";
            nRegistros = ClsConexion.ejecutar(s);
            idCaso = ClsMain.ultimoIden("planeamiento.Casoentidaddeterminacion");
            s="Insert Into planeamiento.Entidaddeterminacionregimen (idcaso, idopciondeterminacion, " +
                    "superposicion) Values (" + idCaso + ", " + idOpcion + ", 0) ";
            nRegistros = ClsConexion.ejecutar(s);

        } catch (Exception e) {
            mLog.error("Fallo en crearCarpetaEntidadesAportadas. " + e + ". Código 23027." );
            return null;
        }
        return iEntidad;
    }

    public static String tieneGeometria(Entidad iEnt){
        String s;
        String tipoGeom="";
        List lista;
        int nPol;
        int nPnt;
        int nLin;

        // Comprueba si la entidad tiene un único tipo de geometría. Los posibles
        //  valores de retorno son:
        //      "Entidadpol"    = tiene, al menos, geometría en esta tabla.
        //      "Entidadpnt"    = tiene, al menos, geometría en esta tabla.
        //      "Entidadlin"    = tiene, al menos, geometría en esta tabla.
        //      ""              = No tiene geometría.
        //      "Error"         = Tiene más de un tipo de geometría.

        try{
            // Primero, se agrupan las geometrías del elemento, para dejar un
            //  máximo de dos.
            ClsMain.agruparGeometrias(iEnt);
            
            lista=new ArrayList();
            s="Select Count(geom) From planeamiento.Entidadpol Where identidad=" + iEnt.getIden();
            lista = ClsConexion.recordList(s);
            nPol=Integer.parseInt(lista.get(0).toString());

            lista=new ArrayList();
            s="Select Count(geom) From planeamiento.Entidadpnt Where identidad=" + iEnt.getIden();
            lista = ClsConexion.recordList(s);
            nPnt=Integer.parseInt(lista.get(0).toString());

            lista=new ArrayList();
            s="Select Count(geom) From planeamiento.Entidadlin Where identidad=" + iEnt.getIden();
            lista = ClsConexion.recordList(s);
            nLin=Integer.parseInt(lista.get(0).toString());

            tipoGeom="";
            if (nPol+nPnt+nLin!=0) {
                // Si sólo tiene geometría de un tipo, es correcto.
                if (nPol<=2 && (nPnt+nLin)==0){
                    tipoGeom="Entidadpol";
                }
                if (nPnt<=2 && (nPol+nLin)==0){
                    tipoGeom="Entidadpnt";
                }
                if (nLin<=2 && (nPnt+nPol)==0){
                    tipoGeom="Entidadlin";
                }
                if (tipoGeom.equals("")){
                    // Error. Tiene más de un tipo de geometría.
                    tipoGeom="Error";
                    mLog.error("Error: la entidad iden=" + iEnt.getIden() + " tiene más de un tipo de geometría. Código 23129.");    
                }                
            }
        } catch(Exception e){
            mLog.error("Fallo en tieneGeometria. " + e + ". Código 23028." );
            tipoGeom="Error";
        }

        return tipoGeom;
    }

    private static void sustitucionNormativa(Entidad iEntO, Entidad iEntR, Boolean completa, int idTramiteOrdenante){
        String s="";
        List listaValoresO;
        List listaValoresR;
        Iterator it;
        int idEntDet;
        Entidaddeterminacion iEntDet;
        List listaRelaciones;
        int idR;
        Relacion iRel;
        int nRegistros;

        // Parámetro Boolean completa:
        //      true = Se tienen en cuenta todos los valores
        //      false = sólo se tienen en cuenta los valores comunes
        //
        // Se borran los valores de la entidad operada, y se copian los valores
        //  de la entidad operadora.
        // Se borran las propiedades y vectores de la entidad operada, y se
        //  reasignan todas las de la operadora a la operada.
        // Se sustituye el nombre de la operada por el de la operadora.
        // Se sustituye la clave de la operada por la de la operadora.
        // Se borran los documentos de la entidad operada, y se reasignan los de la operadora
        //  a la operada.
        // Se sustituye el idEntidadBase de la entidad operada por el de la operadora.

        try {
            // Guarda los valores de iEntO que se deben borrar, y los de iEntR que se deben copiar.

            // Valores a borrar (excepto 'Grupo de Entidades')
            listaValoresO=new ArrayList();
            s="Select ed.iden From planeamiento.Entidaddeterminacion ed, planeamiento.Determinacion d " +
                    "Where ed.identidad=" + iEntO.getIden() + " And ed.iddeterminacion=d.iden " +
                    "And d.idcaracter<>" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " ";
            if (completa==false){
                s = s + "And ed.iddeterminacion In (Select iddeterminacion " +
                        "From planeamiento.Entidaddeterminacion " +
                        "Where identidad=" + iEntR.getIden() + ") ";
            }
            listaValoresO=ClsConexion.recordList(s);

            // Valores a copiar (excepto 'Grupo de Entidades', aunque este filtro también se hace
            //  en el procedimiento ClsMain.copiarEntidadDeterminacion)
            listaValoresR=new ArrayList();
            s="Select ed.iden From planeamiento.Entidaddeterminacion ed, planeamiento.Determinacion d " +
                    "Where ed.identidad=" + iEntR.getIden() + " And ed.iddeterminacion=d.iden " +
                    "And d.idcaracter<>" + ClsDatos.ID_CARACTER_GRUPODEENTIDADES + " ";
            if (completa==false){
                s = s + "And ed.iddeterminacion In (Select iddeterminacion " +
                        "From planeamiento.Entidaddeterminacion " +
                        "Where identidad=" + iEntO.getIden() + ") ";
            }
            listaValoresR=ClsConexion.recordList(s);

            // Eliminar los valores de la entidad operada.
            it=listaValoresO.iterator();
            while (it.hasNext()){
                idEntDet=Integer.parseInt(it.next().toString());
                iEntDet=(Entidaddeterminacion) ClsConexion.dameEntity(Entidaddeterminacion.class, idEntDet);
                ClsMain.eliminarEntidadDeterminacion(iEntDet);
            }

            // Copiar los valores de la entidad operadora a la operada
            if(listaValoresR.size()>0){
                ClsMain.copiarEntidadDeterminacion(iEntO, listaValoresR, false, 0);
            }

            // Se eliminan todas las relaciones de la entidad operada.
            listaRelaciones=new ArrayList();
            listaRelaciones=ClsMain.relacionesPorElemento("ENTIDAD", iEntO.getIden());
            it=listaRelaciones.iterator();
            while (it.hasNext()){
                idR=Integer.parseInt(it.next().toString());
                iRel=(Relacion) ClsConexion.dameEntity(Relacion.class, idR);
                ClsOperacionRelacion.opRel_Eliminacion(iRel, idTramiteOrdenante);
            }

            // Se reasignan todas las relaciones de la entidad operadora a la operada.
            listaRelaciones=new ArrayList();
            listaRelaciones=ClsMain.relacionesPorElemento("ENTIDAD", iEntR.getIden());
            it=listaRelaciones.iterator();
            while (it.hasNext()){
                idR=Integer.parseInt(it.next().toString());
                s="Update planeamiento.Vectorrelacion Set valor=" + iEntO.getIden() + " " +
                        "Where iden In (Select vr.iden From planeamiento.Vectorrelacion vr, " +
                        "diccionario.Defvector dv, diccionario.tabla t " +
                        "Where vr.idrelacion=" + idR + " And vr.iddefvector=dv.iden " +
                        "And dv.idtabla=t.iden And Upper(Trim(t.nombre))='ENTIDAD') " ;

                nRegistros=ClsConexion.ejecutar(s);
            }

            // Se sustituye el nombre de la operada por el de la operadora.
            s="Update planeamiento.Entidad set nombre='" + iEntR.getNombre() + "' " +
                    "Where iden=" + iEntO.getIden() + " ";
            nRegistros=ClsConexion.ejecutar(s);

            // Se sustituye la clave de la operada por la de la operadora.
            s="Update planeamiento.Entidad set clave='" + iEntR.getClave() + "' " +
                    "Where iden=" + iEntO.getIden() + " ";
            nRegistros=ClsConexion.ejecutar(s);

            // Se borran los documentos de la entidad operada, y se reasignan los de la operadora
            //  a la operada.
            s="Delete From planeamiento.Documentoentidad Where identidad=" + iEntO.getIden() + " ";
            nRegistros=ClsConexion.ejecutar(s);
            s="Update planeamiento.Documentoentidad Set identidad=" + iEntO.getIden() + " " +
                    "Where identidad=" + iEntR.getIden() + " ";
            nRegistros=ClsConexion.ejecutar(s);

            // Se sustituye el idEntidadBase, siempre que la operadora no lo
            //  tenga nulo.
            s="Update planeamiento.Entidad set idEntidadBase=(Select identidadbase " +
                    "From planeamiento.Entidad Where iden=" + iEntR.getIden() + " " +
                    "And identidadbase Is Not Null) " +
                    "Where iden=" + iEntO.getIden() + " ";
            nRegistros=ClsConexion.ejecutar(s);
            
        } catch (Exception e){
            if (completa==true){
                mLog.error("Fallo en la operación opEnt_SustitucionNormativaCompleta. " + e + ". Código 23106." );
            } else {
                mLog.error("Fallo en la operación opEnt_SustitucionNormativaParcial. " + e + ". Código 23109." );
            }
        }
    }

    private static boolean bExisteSuperposicionPoligonal(Entidad iEnt1, Entidad iEnt2){
        // Se devuelve TRUE si las geometrías se superponen.

        String s;
        int idEnt1;
        int idEnt2;
        List lista;

        try{
            idEnt1=iEnt1.getIden();
            idEnt2=iEnt2.getIden();

            // Si Ent1 es POL y Ent2 es POL
            lista=new ArrayList();
            s="Select Overlaps(ep1.geom, ep2.geom) From planeamiento.entidadpol ep1, planeamiento.entidadpol ep2 " +
                    "Where ep1.identidad=" + idEnt1 + " And ep2.identidad=" + idEnt2 + " ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }

            // Si Ent1 es POL y Ent2 es LIN
            lista=new ArrayList();
            s="Select Overlaps(ep1.geom, ep2.geom) From planeamiento.entidadpol ep1, planeamiento.entidadlin ep2 " +
                    "Where ep1.identidad=" + idEnt1 + " And ep2.identidad=" + idEnt2 + " ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }

            // Si Ent1 es POL y Ent2 es PNT
            lista=new ArrayList();
            s="Select Overlaps(ep1.geom, ep2.geom) From planeamiento.entidadpol ep1, planeamiento.entidadpnt ep2 " +
                    "Where ep1.identidad=" + idEnt1 + " And ep2.identidad=" + idEnt2 + " ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }

            // Si Ent1 es LIN y Ent2 es POL
            lista=new ArrayList();
            s="Select Overlaps(ep1.geom, ep2.geom) From planeamiento.entidadlin ep1, planeamiento.entidadpol ep2 " +
                    "Where ep1.identidad=" + idEnt1 + " And ep2.identidad=" + idEnt2 + " ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }

            // Si Ent1 es PNT y Ent2 es POL
            lista=new ArrayList();
            s="Select Overlaps(ep1.geom, ep2.geom) From planeamiento.entidadpnt ep1, planeamiento.entidadpol ep2 " +
                    "Where ep1.identidad=" + idEnt1 + " And ep2.identidad=" + idEnt2 + " ";
            lista=ClsConexion.recordList(s);
            if (lista.size()==0){
                return false;
            }
            s=lista.get(0).toString().toUpperCase().trim();
            if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("TRUE")){
                return true;
            }

            // Si no se ha detactado solapamiento
            return false;

        } catch (Exception e) {
            mLog.error("Error al evaluar la superposición de geometrías. " + e + ". Código 23132.");
            return false;
        }

    }

    private static void suspensionYlevantamientoParcial(Entidad iEntO, Entidad iEntR, boolean suspender){
        // Sólo se contempla si la geometría operadora es poligonal
        String s="";
        int nRegistros;
        int idEntOperadora;
        int idEntOperada;
        int srid;
        String tipoGeomR;
        String tipoGeomO;
        Geometry geomO;
        Geometry geomR;
        Geometry geomIntersect;
        
        // Si el parámetro "suspender" es "true" se trata de una suspensión parcial, y si es "false", se trata de
        //  un levantamiento parcial de suspensión.

        // Consiste en dividir su geometría en dos: la entidad original
        //  quedará con la geometría resultante de la sustracción gráfica con la operadora, y
        //  a la entidad nueva se le asigna la intersección de la geometría original con la
        //  de la operadora. Además, a esta nueva geometría se le pone la marca de 'Suspendida=<suspender>'

        // Si la iEntR no tiene geometría, se considera que es una suspensión o levantamiento total.

        // Si iEntO no tiene geometría, no se hace nada, ya que este tipo de operación sólo tiene
        //  carácter gráfico.

        try{
            idEntOperada=iEntO.getIden();
            idEntOperadora=iEntR.getIden();

            // Comprueba si la entidad operada tiene alguna geometría. Si no es así, no se hace nada.
            if (tieneGeometria(iEntO).equalsIgnoreCase("")){
                return;
            }

            // Comprueba si la entidad operadora tiene alguna geometría. Si no es así, se considera que la
            //  suspensión o el levantamiento es total.
            tipoGeomR=tieneGeometria(iEntR);
            if (tipoGeomR.equalsIgnoreCase("")){
                if (suspender==true){
                    opEnt_SuspensionTotal(iEntO, iEntR);
                } else {
                    opEnt_LevantamientoTotalSuspension(iEntO, iEntR);
                }
                return;
            }

            // Si la entidad operadora no es poligonal, no se puede efectuar la operación
            if (!tipoGeomR.equalsIgnoreCase("EntidadPol")){
                mLog.warn("Aviso: No puede efectuarse la operación debido a que la entidad operadora no es poligonal.");
                return;
            }

            // Comprueba si las geometrías intersectan. Si no es así, no se hace nada.
            if (bExisteSuperposicionPoligonal(iEntO, iEntR)==false){
                return;
            }

            // Averigua el tipo de geometría de la entidad operada. Si no tiene, se 
            //  considera que la suspensión o el levantamiento es total.
            tipoGeomO=tieneGeometria(iEntO);
            if (tipoGeomO.equalsIgnoreCase("")){
                if (suspender==true){
                    opEnt_SuspensionTotal(iEntO, iEntR);
                } else {
                    opEnt_LevantamientoTotalSuspension(iEntO, iEntR);
                }
                return;
            }
            
            // Calcula la geometría. La nueva geometría es igual a la intersección de
            //  la operada con la operadora (con bSuspendida=<suspender>), y la geometría de la
            //  operada queda como la sustracción con la operadora (sin cambiar bSuspendida)
            geomO=ClsMain.geometria( tipoGeomO, idEntOperada, !suspender);
            geomR=ClsMain.geometria( tipoGeomR, idEntOperadora, false);
            geomIntersect=ClsMain.operarGeometrias(geomO, geomR, 2);
            geomIntersect=ClsMain.limpiarGeometria(tipoGeomO, geomIntersect);
            if (geomIntersect==null){
                return;
            }
            geomIntersect=ClsMain.limpiarGeometria(tipoGeomO, geomIntersect);
            if (geomIntersect.isEmpty()==true){
                return;
            }
            
            // Modifica la geometría de la entidad operada, haciendo la sustracción con la operadora
            opEnt_SustraccionGrafica(iEntO, iEntR, 0);
            // Insert la nueva geometría, a la cual se le pone el valor de bSuspendida
            srid=ClsMain.srId(tipoGeomO, idEntOperada);
            
            /* FGA: Este era el original
             
              s = "Insert Into planeamiento." + tipoGeomO + " (identidad, geom, bsuspendida) " +
                    "Values (" + idEntOperada + ", geom=multi(geometryfromtext('" + 
                    geomIntersect + "', "+ srid + ")), " + suspender + ")";
                    
             */
            s = "Insert Into planeamiento." + tipoGeomO + " (identidad, geom, bsuspendida) " +
                    "Values (" + idEntOperada + ", multi(geometryfromtext('" + 
                    geomIntersect + "', "+ srid + ")), " + suspender + ")";
            nRegistros=ClsConexion.ejecutar(s);
            if (nRegistros==0){
                s="Aviso: no se ha conseguido insertar la geometría de la " + 
                    "entidad iden=" + idEntOperada + " con suspensión=" + suspender;
                mLog.warn(s );
            }
            
        } catch (Exception e){
            mLog.error("Error: fallo en la operación opEnt_SuspensionParcial. " + e + ". Código 23122." );
        }
    }

    
}
